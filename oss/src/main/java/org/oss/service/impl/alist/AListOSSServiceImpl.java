package org.oss.service.impl.alist;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.SaveConfig;
import org.core.utils.ExceptionUtil;
import org.jetbrains.annotations.Nullable;
import org.oss.service.OSSService;
import org.oss.service.impl.alist.model.list.ContentItem;
import org.oss.service.impl.alist.util.RequestUtils;
import org.springframework.util.DigestUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class AListOSSServiceImpl implements OSSService {
    
    private static final String SERVICE_NAME = "AList";
    
    private static final String LOGIN_KEY = "loginKey";
    
    // 音乐地址创建缓存
    public static final TimedCache<String, ContentItem> musicUrltimedCache = CacheUtil.newTimedCache(1000L * 60L * 60L);
    // 创建登录缓存
    public static final TimedCache<String, String> loginTimeCache = CacheUtil.newTimedCache(1000L * 60L * 60L);
    // 第一次获取所有歌曲量, 对比缓存，如果不相同则自动刷新
    private Integer initMusicAllCount = 0;
    
    private SaveConfig config;
    
    static {
        musicUrltimedCache.schedulePrune(1000);
    }
    
    
    @Override
    public String getMode() {
        return SERVICE_NAME;
    }
    
    /**
     * 只会返回true,登录错误直接抛出异常
     *
     * @param config 保存信息
     */
    @Override
    public boolean isConnected(SaveConfig config) {
        this.config = config;
        this.config.setAssignObjectSave(config.getAssignObjectSave() == null ? 0 : config.getAssignObjectSave());
        String loginCacheStr = loginTimeCache.get(LOGIN_KEY);
        if (StringUtils.isBlank(loginCacheStr)) {
            String login = login(config.getHost(), config.getAccessKey(), config.getSecretKey());
            loginTimeCache.put(LOGIN_KEY, login);
            ExceptionUtil.isNull(StringUtils.isBlank(loginTimeCache.get(LOGIN_KEY)), ResultCode.OSS_LOGIN_ERROR);
        }
        return true;
    }
    
    @Override
    public void isExist(String name) {
        getAddresses(name, true);
    }
    
    @Override
    public String getAddresses(String name, boolean refresh) {
        try {
            String loginCacheStr = getLoginJwtCache(config);
            // 音乐地址URL缓存
            ContentItem item = musicUrltimedCache.get(name);
            // 没有地址便刷新缓存,获取所有文件保存到缓存中
            // 第一次执行，必须刷新缓存。所以添加添加缓存是否存在条件
            if ((item == null && refresh) || musicUrltimedCache.isEmpty() || musicUrltimedCache.size() != initMusicAllCount) {
                refreshMusicCache(loginCacheStr);
                // 更新初始化音乐数量
                this.initMusicAllCount = musicUrltimedCache.size();
                item = musicUrltimedCache.get(name);
            }
            // 没有找到文件直接抛出异常
            if (item == null || StringUtils.isBlank(item.getPath())) {
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
            String loginCacheStr = getLoginJwtCache(config);
            // 音乐地址URL缓存
            Set<ContentItem> collect = name.parallelStream().map(musicUrltimedCache::get).collect(Collectors.toSet());
            // 没有地址便刷新缓存,获取所有文件保存到缓存中
            // 第一次执行，必须刷新缓存。所以添加添加缓存是否存在条件
            if ((refresh && CollUtil.isEmpty(collect)) || musicUrltimedCache.isEmpty() || musicUrltimedCache.size() != initMusicAllCount) {
                refreshMusicCache(loginCacheStr);
                // 更新初始化音乐数量
                this.initMusicAllCount = musicUrltimedCache.size();
                collect = name.parallelStream().map(musicUrltimedCache::get).collect(Collectors.toSet());
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
    
    /**
     * 获取音乐地址
     *
     * @param md5     音乐文件文件MD5
     * @param refresh 是否刷新缓存
     * @return 音乐地址
     */
    @Override
    public Map<String, Map<String, String>> getAddressByMd5(String md5, boolean refresh) {
        try {
            String loginCacheStr = getLoginJwtCache(config);
            // 音乐地址URL缓存
            Iterator<ContentItem> set = musicUrltimedCache.iterator();
            // 没有地址便刷新缓存,获取所有文件保存到缓存中
            // 第一次执行，必须刷新缓存。所以添加添加缓存是否存在条件
            if (musicUrltimedCache.isEmpty() || musicUrltimedCache.size() != initMusicAllCount) {
                refreshMusicCache(loginCacheStr);
                // 更新初始化音乐数量
                this.initMusicAllCount = musicUrltimedCache.size();
                set = musicUrltimedCache.iterator();
            }
            HashMap<String, Map<String, String>> map = new HashMap<>();
            if (StringUtils.isBlank(md5)) {
                set.forEachRemaining(contentItem -> getPathMap(map, contentItem));
            } else {
                set.forEachRemaining(contentItem -> {
                    if (StringUtils.startsWithIgnoreCase(StringUtils.split(contentItem.getName(), ".")[0], md5)) {
                        getPathMap(map, contentItem);
                    }
                });
            }
            return map;
        } catch (BaseException e) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST.getCode(), e.getResultMsg());
        }
    }
    
    public static final String SIZE = "size";
    public static final String URL = "url";
    
    private void getPathMap(HashMap<String, Map<String, String>> map, ContentItem contentItem) {
        String path = getPath(contentItem);
        HashMap<String, String> value = new HashMap<>();
        value.put(SIZE, String.valueOf(contentItem.getSize()));
        value.put(URL, path);
        map.put(contentItem.getName(), value);
    }
    
    private String getPath(ContentItem contentItem) {
        String path = StringUtils.startsWith(contentItem.getPath(), "/") ? StringUtils.substring(contentItem.getPath(), 1) : contentItem.getPath();
        return String.format("%s/d/%s/%s?sign=%s",
                config.getHost(),
                path,
                contentItem.getName(),
                contentItem.getSign());
    }
    
    private void refreshMusicCache(String loginCacheStr) {
        ArrayList<String> col = new ArrayList<>();
        col.addAll(config.getObjectSave());
        col.addAll(config.getImgSave());
        for (String s : col) {
            List<ContentItem> list = RequestUtils.list(config.getHost(), s, loginCacheStr);
            if (CollUtil.isEmpty(list)) {
                continue;
            }
            list.parallelStream().forEach(contentItem -> {
                contentItem.setPath(s);
                musicUrltimedCache.put(contentItem.getName(), contentItem);
            });
        }
    }
    
    private String login(String host, String accessKey, String secretKey) {
        return RequestUtils.login(host, accessKey, secretKey);
    }
    
    @Nullable
    private String getLoginJwtCache(SaveConfig config) {
        String loginCacheStr = loginTimeCache.get(LOGIN_KEY);
        if (StringUtils.isBlank(loginCacheStr)) {
            boolean connected = isConnected(config);
            if (!connected) {
                throw new BaseException(ResultCode.OSS_LOGIN_ERROR);
            }
            loginCacheStr = loginTimeCache.get(LOGIN_KEY);
        }
        return loginCacheStr;
    }
    
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
                BufferedInputStream inputStream = FileUtil.getInputStream(srcFile);
                String tempMd5 = DigestUtils.md5DigestAsHex(inputStream);
                inputStream.close();
                musicAddresses = getAddresses(tempMd5, false);
            } else {
                musicAddresses = getAddresses(md5, false);
            }
            if (StringUtils.isNotBlank(musicAddresses)) {
                return srcFile.getName();
            }
        } catch (BaseException e) {
            if (!StringUtils.equals(e.getCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                throw new BaseException(e.getCode(), e.getResultMsg());
            }
        } catch (IOException e) {
            throw new BaseException(e.getMessage());
        }
        String loginJwtCache = getLoginJwtCache(config);
        String path = RequestUtils.upload(config.getHost(), paths.get(index), srcFile, loginJwtCache);
        // 校验是否上传成功
        try {
            getAddresses(path, true);
        } catch (Exception e) {
            throw new BaseException(ResultCode.OSS_UPLOAD_ERROR);
        }
        return path;
    }
    
    /**
     * 获取所有音乐下载地址
     *
     * @param md5     音乐的md5值
     * @param refresh 是否刷新缓存
     * @return 音乐下载地址
     */
    @Override
    public Collection<String> getAllMD5(String md5, boolean refresh) {
        try {
            String loginCacheStr = getLoginJwtCache(config);
            // 音乐地址URL缓存
            // 没有地址便刷新缓存,获取所有文件保存到缓存中
            // 第一次执行，必须刷新缓存。所以添加添加缓存是否存在条件
            int size = musicUrltimedCache.size();
            if (refresh || musicUrltimedCache.isEmpty() || size != initMusicAllCount) {
                refreshMusicCache(loginCacheStr);
                // 更新初始化音乐数量
                this.initMusicAllCount = size;
            }
            // 没有找到文件直接抛出异常
            ArrayList<String> res = new ArrayList<>(size);
            if (StringUtils.isNotBlank(md5)) {
                for (ContentItem next : musicUrltimedCache) {
                    String cs1 = StringUtils.split(next.getName(), ',')[0];
                    if (StringUtils.equals(cs1, md5)) {
                        return Collections.singletonList(cs1);
                    }
                }
            }
            
            musicUrltimedCache.forEach(contentItem -> res.add(Optional.ofNullable(StringUtils.split(contentItem.getName(), "."))
                                                                      .orElse(new String[]{""})[0]));
            return res;
        } catch (BaseException e) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST.getCode(), e.getResultMsg());
        }
    }
    
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
        String loginCacheStr = getLoginJwtCache(config);
        // 音乐地址URL缓存
        Map<String, ArrayList<ContentItem>> collect = name.parallelStream()
                                                          .map(musicUrltimedCache::get)
                                                          .collect(Collectors.toMap(ContentItem::getPath, ListUtil::toList, (objects, objects2) -> {
                                                              objects2.addAll(objects);
                                                              return objects2;
                                                          }));
        RequestUtils.delete(config.getHost(), collect, loginCacheStr);
        return true;
    }
}
