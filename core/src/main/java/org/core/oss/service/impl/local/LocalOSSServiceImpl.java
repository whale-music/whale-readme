package org.core.oss.service.impl.local;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.properties.SaveConfig;
import org.core.common.result.ResultCode;
import org.core.mybatis.iservice.TbResourceService;
import org.core.mybatis.pojo.TbResourcePojo;
import org.core.oss.service.OSSService;
import org.core.oss.service.impl.local.model.FileMetadata;
import org.core.utils.ServletUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service(LocalOSSServiceImpl.SERVICE_NAME)
public class LocalOSSServiceImpl implements OSSService {
    
    // 音乐地址创建缓存
    public static final TimedCache<String, FileMetadata> MUSIC_PATH_CACHE = CacheUtil.newTimedCache(1000L * 60L * 60L);
    public static final String SIZE = "size";
    public static final String URL = "url";
    public static final String SERVICE_NAME = "Local";
    private SaveConfig config;
    private int initMusicAllCount;
    
    private final TbResourceService tbResourceService;
    
    public LocalOSSServiceImpl(TbResourceService tbResourceService) {
        this.tbResourceService = tbResourceService;
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
     *
     * @param config 服务配置
     */
    @Override
    public void isConnected(SaveConfig config) {
        this.config = config;
        String host = config.getHost();
        File mkdir = FileUtil.mkdir(host);
        boolean directory = FileUtil.isDirectory(mkdir);
        if (Boolean.FALSE.equals(directory)) {
            throw new BaseException(ResultCode.STORAGE_PATH_DOES_NOT_EXIST);
        }
    }
    
    /**
     * 存储文件是否存在
     *
     * @param name 文件名
     */
    @Override
    public void isExist(String name) {
        getAddresses(name, true);
    }
    
    /**
     * 获取音乐地址
     *
     * @param name    音乐文件文件地址
     * @param refresh 是否刷新缓存
     * @return 音乐地址
     */
    @Override
    public String getAddresses(String name, boolean refresh) {
        try {
            // 音乐地址URL缓存
            FileMetadata item = MUSIC_PATH_CACHE.get(name);
            // 没有地址便刷新缓存,获取所有文件保存到缓存中
            // 第一次执行，必须刷新缓存。所以添加添加缓存是否存在条件
            if ((item == null && refresh) || MUSIC_PATH_CACHE.isEmpty() || MUSIC_PATH_CACHE.size() != initMusicAllCount) {
                refreshMusicCache();
                // 更新初始化音乐数量
                this.initMusicAllCount = MUSIC_PATH_CACHE.size();
                item = MUSIC_PATH_CACHE.get(name);
            }
            // 没有找到文件直接抛出异常
            if (item == null || StringUtils.isBlank(item.getUri())) {
                throw new BaseException(ResultCode.DATA_NONE_FOUND.getCode(), ResultCode.DATA_NONE_FOUND.getResultMsg() + ": " + name);
            }
            return getPath(item);
        } catch (BaseException e) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST.getCode(), e.getResultMsg());
        }
    }
    
    /**
     * 获取音乐地址列表
     *
     * @param name    音乐集合
     * @param refresh 是否刷新缓存
     * @return 音乐地址集合
     */
    @Override
    public Set<String> getAddresses(Collection<String> name, boolean refresh) {
        try {
            // 音乐地址URL缓存
            Set<FileMetadata> collect = name.parallelStream().map(MUSIC_PATH_CACHE::get).collect(Collectors.toSet());
            // 没有地址便刷新缓存,获取所有文件保存到缓存中
            // 第一次执行，必须刷新缓存。所以添加添加缓存是否存在条件
            if ((refresh && CollUtil.isEmpty(collect)) || MUSIC_PATH_CACHE.isEmpty() || MUSIC_PATH_CACHE.size() != initMusicAllCount) {
                refreshMusicCache();
                // 更新初始化音乐数量
                this.initMusicAllCount = MUSIC_PATH_CACHE.size();
                collect = name.parallelStream().map(MUSIC_PATH_CACHE::get).collect(Collectors.toSet());
            }
            // 没有找到文件直接抛出异常
            if (CollUtil.isEmpty(collect)) {
                throw new BaseException(ResultCode.DATA_NONE_FOUND.getCode(), ResultCode.DATA_NONE_FOUND.getResultMsg() + ": " + name);
            }
            return collect.stream().map(this::getPath).collect(Collectors.toSet());
        } catch (BaseException e) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST.getCode(), e.getResultMsg());
        }
    }
    
    private void refreshMusicCache() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(config.getObjectSave());
        list.addAll(config.getImgSave());
        list.addAll(config.getMvSave());
        
        for (String parentFileName : list) {
            File file = new File(config.getHost(), parentFileName);
            FileUtil.mkdir(file);
            List<String> files = FileUtil.listFileNames(file.getAbsolutePath());
            if (CollUtil.isEmpty(files)) {
                continue;
            }
            for (String filename : files) {
                File item = new File(file.getPath(), filename);
                FileMetadata meta = new FileMetadata();
                String md5 = FileUtil.mainName(filename);
                meta.setFullPath(item.getPath());
                String uri = parentFileName = parentFileName.charAt(parentFileName.length() - 1) == '/' ? parentFileName : parentFileName + '/';
                meta.setUri(uri + filename);
                meta.setFileMd5(md5);
                meta.setSize(FileUtil.size(item));
                meta.setName(item.getName());
                meta.setLastAccessTime(FileUtil.lastModifiedTime(item));
                try {
                    BasicFileAttributes basicFileAttributes = Files.readAttributes(item.toPath(), BasicFileAttributes.class);
                    meta.setCreationTime(new Date(basicFileAttributes.creationTime().toMillis()));
                    meta.setLastAccessTime(new Date(basicFileAttributes.lastAccessTime().toMillis()));
                } catch (IOException e) {
                    meta.setCreationTime(new Date());
                    meta.setLastAccessTime(new Date());
                    log.warn("read file no create data: {}", e.getMessage());
                }
                MUSIC_PATH_CACHE.put(item.getName(), meta);
            }
        }
        
    }
    
    private String getPath(FileMetadata contentItem) {
        String path = contentItem.getUri().charAt(0) == '/' ? StringUtils.substring(contentItem.getUri(), 1) : contentItem.getUri();
        // 不能使用ServletUtils.getRequest().getRemoteHost(), 因为有时访问localhost,却返回ipv6. 所以直接返回访问域名
        String remoteHost = URLUtil.url(ServletUtils.getRequest().getRequestURL().toString()).getHost();
        int serverPort = ServletUtils.getRequest().getServerPort();
        String scheme = ServletUtils.getRequest().getScheme();
        // 127.0.0.1:6780/d/music/7694f4a66316e53c8cdd9d9954bd611d.mp3
        String resourcePrefix = "common/static";
        return String.format("%s://%s:%d/%s/%s", scheme, remoteHost, serverPort, resourcePrefix, URLEncodeUtil.encodeQuery(path));
    }
    
    private void getPathMap(HashMap<String, Map<String, String>> map, FileMetadata contentItem) {
        String path = getPath(contentItem);
        HashMap<String, String> value = new HashMap<>();
        value.put(SIZE, String.valueOf(contentItem.getSize()));
        value.put(URL, path);
        map.put(contentItem.getName(), value);
    }
    
    /**
     * 获取音乐地址
     *
     * @param md5Set  音乐文件文件MD5
     * @param refresh 是否刷新缓存
     * @return 音乐地址 key md5, value url, size
     */
    @Override
    public Map<String, Map<String, String>> getAddressByMd5(Set<String> md5Set, boolean refresh) {
        // 音乐地址URL缓存
        Iterator<FileMetadata> set = MUSIC_PATH_CACHE.iterator();
        // 没有地址便刷新缓存,获取所有文件保存到缓存中
        // 第一次执行，必须刷新缓存。所以添加添加缓存是否存在条件
        if (MUSIC_PATH_CACHE.isEmpty() || MUSIC_PATH_CACHE.size() != initMusicAllCount) {
            refreshMusicCache();
            // 更新初始化音乐数量
            this.initMusicAllCount = MUSIC_PATH_CACHE.size();
            set = MUSIC_PATH_CACHE.iterator();
        }
        HashMap<String, Map<String, String>> map = new HashMap<>();
        if (CollUtil.isEmpty(md5Set)) {
            set.forEachRemaining(contentItem -> getPathMap(map, contentItem));
        } else {
            for (String md5 : md5Set) {
                set.forEachRemaining(contentItem -> {
                    TbResourcePojo one = tbResourceService.getOne(Wrappers.<TbResourcePojo>lambdaQuery()
                                                                          .eq(TbResourcePojo::getPath, contentItem.getName()));
                    if (StringUtils.equals(one.getMd5(), md5)) {
                        getPathMap(map, contentItem);
                    }
                });
            }
        }
        return map;
    }
    
    /**
     * 获取音乐MD5值，为null获取所有md5
     *
     * @param md5     音乐的md5值
     * @param refresh 是否刷新缓存
     * @return MD5值
     */
    @Override
    public Collection<String> getResourceMD5(String md5, boolean refresh) {
        // 音乐地址URL缓存
        // 没有地址便刷新缓存,获取所有文件保存到缓存中
        // 第一次执行，必须刷新缓存。所以添加添加缓存是否存在条件
        int size = MUSIC_PATH_CACHE.size();
        if (refresh || MUSIC_PATH_CACHE.isEmpty() || size != initMusicAllCount) {
            refreshMusicCache();
            // 更新初始化音乐数量
            this.initMusicAllCount = MUSIC_PATH_CACHE.size();
        }
        // 没有找到文件直接抛出异常
        ArrayList<String> res = new ArrayList<>(size);
        if (StringUtils.isNotBlank(md5)) {
            for (FileMetadata next : MUSIC_PATH_CACHE) {
                String cs1 = StringUtils.split(next.getName(), ',')[0];
                if (StringUtils.equals(cs1, md5)) {
                    return Collections.singletonList(cs1);
                }
            }
        }
        
        MUSIC_PATH_CACHE.forEach(contentItem -> res.add(Optional.ofNullable(StringUtils.split(contentItem.getName(), "."))
                                                                .orElse(new String[]{""})[0]));
        return res;
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
    public String upload(List<String> paths, Integer index, File srcFile, String md5) {
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
                musicAddresses = this.getAddresses(tempMd5, false);
            } else {
                musicAddresses = this.getAddresses(md5, false);
            }
            if (StringUtils.isNotBlank(musicAddresses)) {
                return srcFile.getName();
            }
        } catch (BaseException e) {
            if (!StringUtils.equals(e.getCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                throw new BaseException(e.getCode(), e.getResultMsg());
            }
        }
        String pathname = paths.get(index);
        pathname = StringUtils.startsWith(pathname, "/") ? pathname : pathname + FileUtil.FILE_SEPARATOR;
        pathname = StringUtils.replace(pathname, "/", FileUtil.FILE_SEPARATOR);
        pathname = StringUtils.replace(pathname, "\\", FileUtil.FILE_SEPARATOR);
        
        File dest = new File(config.getHost() + FileUtil.FILE_SEPARATOR + pathname, srcFile.getName());
        File file = FileUtil.copy(srcFile, dest, true);
        // 校验是否上传成功
        try {
            getAddresses(file.getName(), true);
        } catch (Exception e) {
            throw new BaseException(ResultCode.OSS_UPLOAD_ERROR);
        }
        return file.getName();
    }
    
    /**
     * 删除文件
     *
     * @param name 文件名
     * @return 是否删除成功
     */
    @Override
    public boolean delete(List<String> name) {
        try {
            // 忽略无文件错误
            isExist(name.get(0));
        } catch (BaseException e) {
            if (StringUtils.equals(e.getCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                return false;
            } else {
                throw new BaseException(e.getCode(), e.getResultMsg());
            }
        }
        // 音乐地址URL缓存
        Map<String, ArrayList<FileMetadata>> collect = name.parallelStream()
                                                           .map(MUSIC_PATH_CACHE::get)
                                                           .filter(Objects::nonNull)
                                                           .collect(Collectors.toMap(FileMetadata::getUri, ListUtil::toList, (objects, objects2) -> {
                                                               objects2.addAll(objects);
                                                               return objects2;
                                                           }));
        
        for (List<FileMetadata> value : collect.values()) {
            for (FileMetadata fileMetadata : value) {
                FileUtil.del(fileMetadata.getFullPath());
            }
        }
        return true;
    }
    
    /**
     * 重命名
     *
     * @param oldName 旧文件名
     * @param newName 新文件名
     */
    @Override
    public void rename(String oldName, String newName) {
        isExist(oldName);
        FileMetadata metadata = MUSIC_PATH_CACHE.get(oldName);
        FileUtil.rename(new File(metadata.getFullPath()), newName, true);
    }
}
