package org.core.oss.service.impl.local;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.properties.SaveConfig;
import org.core.common.result.ResultCode;
import org.core.mybatis.iservice.TbResourceService;
import org.core.mybatis.pojo.TbResourcePojo;
import org.core.oss.model.Resource;
import org.core.oss.service.OSSService;
import org.core.oss.service.OSSServiceAbs;
import org.core.oss.service.impl.alist.enums.ResourceEnum;
import org.core.utils.ServletUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service(LocalOSSServiceImpl.SERVICE_NAME)
public class LocalOSSServiceImpl extends OSSServiceAbs implements OSSService {
    
    public static final String SERVICE_NAME = "local";
    private final TbResourceService tbResourceService;
    
    public LocalOSSServiceImpl(SaveConfig config, TbResourceService tbResourceService) {
        this.config = config;
        this.tbResourceService = tbResourceService;
        
        long timeout = this.config.getBufferTime();
        musicUrlTimedCache = CacheUtil.newTimedCache(timeout);
        picUrlTimedCache = CacheUtil.newTimedCache(timeout);
        mvUrlTimedCache = CacheUtil.newTimedCache(timeout);
        musicMd5TimedCache = CacheUtil.newTimedCache(timeout);
        picMd5TimedCache = CacheUtil.newTimedCache(timeout);
        mvMd5TimedCache = CacheUtil.newTimedCache(timeout);
        
        int delay = 60 * 1000;
        musicUrlTimedCache.schedulePrune(delay);
        picUrlTimedCache.schedulePrune(delay);
        mvUrlTimedCache.schedulePrune(delay);
        
        cache.put(ResourceEnum.MUSIC, musicUrlTimedCache);
        cache.put(ResourceEnum.PIC, picUrlTimedCache);
        cache.put(ResourceEnum.MV, mvUrlTimedCache);
        
        cacheMd5.put(ResourceEnum.MUSIC, musicMd5TimedCache);
        cacheMd5.put(ResourceEnum.PIC, picMd5TimedCache);
        cacheMd5.put(ResourceEnum.MV, mvMd5TimedCache);
    }
    
    /**
     * 返回当前服务名
     *
     * @return 服务名
     */
    @Override
    public String getMode() {
        return SERVICE_NAME;
    }
    
    /**
     * 检查访问存储地址
     */
    @Override
    public void isConnected() {
        String host = config.getHostMapping();
        File mkdir = FileUtil.mkdir(host);
        boolean directory = FileUtil.isDirectory(mkdir);
        if (Boolean.FALSE.equals(directory)) {
            throw new BaseException(ResultCode.STORAGE_PATH_DOES_NOT_EXIST);
        }
    }
    
    /**
     * 根据路径获取数据
     *
     * @param paths   路径列表
     * @param refresh 是否刷新
     * @param type    查询数据类型
     * @return 资源数据
     */
    @Override
    public Map<String, Resource> getResourceList(Collection<String> paths, boolean refresh, ResourceEnum type) {
        TimedCache<String, Resource> cacheResources = cache.get(type);
        TimedCache<String, String> cacheTimeMd5 = this.cacheMd5.get(type);
        Map<String, Resource> resourceMap = new HashMap<>();
        
        if (Boolean.TRUE.equals(refresh) || CollUtil.isEmpty(resourceMap)) {
            cacheResources.clear();
            cacheTimeMd5.clear();
            scanResource(config.getDeep(), type);
        }
        conditionGetItemByPath(paths, cacheResources, resourceMap);
        return resourceMap;
    }
    
    
    private void scanResource(Integer deep, ResourceEnum type) {
        Set<String> col = new LinkedHashSet<>();
        correspondingData(type, col);
        
        TimedCache<String, Resource> cacheResources = cache.get(type);
        TimedCache<String, String> md5MappingPath = cacheMd5.get(type);
        for (String s : col) {
            List<File> files = PathUtil.loopFiles(Path.of(config.getHost() + "/" + s), deep, pathname -> true);
            for (File file : files) {
                Resource e = new Resource();
                e.setName(file.getName());
                String relativePath = Paths.get(config.getHost()).relativize(Paths.get(file.getPath())).toString();
                e.setPath(CharSequenceUtil.addPrefixIfNot(stringSlashConversion(relativePath, "/"), "/"));
                String path = getPath(relativePath);
                e.setUrl(path);
                e.setHref(file.getAbsolutePath());
                e.setSize(file.length());
                
                TbResourcePojo tbResourcePojo = dbCache.get(e.getPath(), () -> tbResourceService.getResourceByPath(e.getPath()));
                if (Objects.nonNull(tbResourcePojo)) {
                    md5MappingPath.put(tbResourcePojo.getMd5(), e.getPath());
                }
                cacheResources.put(e.getPath(), e);
            }
        }
    }
    
    private String getPath(final String uri) {
        String path = CharSequenceUtil.removePrefix(stringSlashConversion(uri, "/"), "/");
        // 不能使用ServletUtils.getRequest().getRemoteHost(), 因为有时访问localhost,却返回ipv6. 所以直接返回访问域名
        String remoteHost = URLUtil.url(ServletUtils.getRequest().getRequestURL().toString()).getHost();
        int serverPort = ServletUtils.getRequest().getServerPort();
        String scheme = ServletUtils.getRequest().getScheme();
        // 127.0.0.1:6780/d/music/7694f4a66316e53c8cdd9d9954bd611d.mp3
        String resourcePrefix = "common/static";
        return String.format("%s://%s:%d/%s/%s", scheme, remoteHost, serverPort, resourcePrefix, URLEncodeUtil.encodeQuery(path));
    }
    
    /**
     * 获取音乐地址
     *
     * @param md5Set  音乐文件文件MD5
     * @param refresh 是否刷新缓存
     * @param type    查询数据类型
     * @return 音乐地址 key md5, value url, size
     */
    @Override
    public Map<String, Resource> getResourceByMd5ToMap(Collection<String> md5Set, boolean refresh, ResourceEnum type) {
        TimedCache<String, Resource> cacheResources = cache.get(type);
        TimedCache<String, String> md5Mapping = cacheMd5.get(type);
        Map<String, Resource> resourceMap = new HashMap<>();
        
        if (Boolean.TRUE.equals(refresh) || CollUtil.isEmpty(resourceMap)) {
            cacheResources.clear();
            md5Mapping.clear();
            scanResource(config.getDeep(), type);
        }
        conditionGetItemByMd5(md5Set, cacheResources, md5Mapping, resourceMap);
        return resourceMap;
    }
    
    /**
     * 上传文件返回地址
     *
     * @param paths   路径
     * @param index   选中上传路径
     * @param srcFile 上传文件
     * @param md5     上传文件md5 非必传
     * @return 文件路径相对
     */
    @Override
    public String upload(List<String> paths, Integer index, File srcFile, String md5, ResourceEnum type) {
        try {
            if (FileUtil.isDirectory(srcFile)) {
                throw new BaseException(ResultCode.FILENAME_INVALID);
            }
            long size = FileUtil.size(srcFile);
            if (size == 0) {
                throw new BaseException(ResultCode.FILE_SIZE_CANNOT_BE_ZERO);
            }
            String musicAddresses;
            if (StringUtils.isEmpty(md5)) {
                String tempMd5 = DigestUtil.md5Hex(srcFile);
                musicAddresses = this.getResourceByMd5(tempMd5, false, type).getUrl();
            } else {
                musicAddresses = this.getResourceByMd5(md5, false, type).getUrl();
            }
            if (StringUtils.isNotBlank(musicAddresses)) {
                return srcFile.getName();
            }
        } catch (BaseException e) {
            if (!StringUtils.equals(e.getCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                throw new BaseException(e.getCode(), e.getResultMsg());
            }
        }
        final String fileSeparator = FileUtil.FILE_SEPARATOR;
        String pathname = CharSequenceUtil.addSuffixIfNot(stringSlashConversion(paths.get(index), fileSeparator), fileSeparator);
        String parent = config.getHost() + CharSequenceUtil.addPrefixIfNot(pathname, fileSeparator);
        File dest = new File(parent, srcFile.getName());
        File file = FileUtil.copy(srcFile, dest, true);
        String path = stringSlashConversion(CharSequenceUtil.addSuffixIfNot(pathname, "\\") + file.getName(), "/");
        // 校验是否上传成功
        try {
            // 返回绝对路径
            isExist(path, type);
        } catch (BaseException e) {
            throw new BaseException(ResultCode.OSS_UPLOAD_ERROR);
        }
        return path;
    }
    
    /**
     * 删除文件
     *
     * @param paths 文件名
     * @param type  查询数据类型
     */
    @Override
    public void delete(List<String> paths, ResourceEnum type) {
        TimedCache<String, Resource> resources = cache.get(type);
        for (String s : paths) {
            try {
                // 忽略无文件错误
                isExist(s, type);
            } catch (BaseException e) {
                if (StringUtils.equals(e.getCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                    paths.remove(s);
                } else {
                    throw new BaseException(e.getCode(), e.getResultMsg());
                }
            }
        }
        // 音乐地址URL缓存
        Map<String, List<Resource>> collect = paths.parallelStream()
                                                   .map(resources::get)
                                                   .filter(Objects::nonNull)
                                                   .collect(Collectors.toMap(Resource::getPath, ListUtil::toList, (objects, objects2) -> {
                                                       objects2.addAll(objects);
                                                       return objects2;
                                                   }));
        
        for (List<Resource> value : collect.values()) {
            for (Resource fileMetadata : value) {
                FileUtil.del(fileMetadata.getHref());
            }
        }
    }
    
    /**
     * 重命名
     *
     * @param path    旧文件名
     * @param newName 新文件名
     */
    @Override
    public String rename(String path, String newName, ResourceEnum type) {
        isExist(path, type);
        Resource resource = this.getResource(path, false, type);
        FileUtil.rename(new File(resource.getHref()), newName, true);
        return getReName(path, newName);
    }
}
