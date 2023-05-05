package org.oss.service.impl.alist;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.sun.net.httpserver.Headers;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.SaveConfig;
import org.core.utils.ExceptionUtil;
import org.jetbrains.annotations.Nullable;
import org.oss.service.OSSService;
import org.oss.service.impl.alist.model.list.ContentItem;
import org.oss.service.impl.alist.util.RequestUtils;

import java.io.File;
import java.util.*;

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
    public boolean isCurrentOSS(String serviceName) {
        return StringUtils.equals(SERVICE_NAME, serviceName);
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
        getMusicAddresses(name, true);
    }
    
    @Override
    public String getMusicAddresses(String name, boolean refresh) {
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
            if (item == null) {
                throw new BaseException(ResultCode.DATA_NONE.getCode(), ResultCode.DATA_NONE.getResultMsg() + ": " + name);
            }
            String musicAddress = String.format("%s/d/%s/%s?sign=%s", config.getHost(), item.getPath(), name, item.getSign());
            if (StringUtils.isBlank(musicAddress)) {
                throw new BaseException();
            } else {
                return musicAddress;
            }
        } catch (BaseException e) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST.getCode(), e.getErrorMsg());
        }
    }
    
    private void refreshMusicCache(String loginCacheStr) {
        Headers headers = new Headers();
        headers.put("Authorization", Collections.singletonList(loginCacheStr));
        for (String s : config.getObjectSave()) {
            List<ContentItem> list = RequestUtils.list(config.getHost(), s, headers);
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
    
    public String getMonoMusicAddress(String host, String objectSave, String path) {
        try {
            String sign = RequestUtils.getMusicAddress(host, objectSave, path);
            String musicAddress = String.format("%s/d/%s/%s?sign=%s", host, objectSave, path, sign);
            if (StringUtils.isBlank(musicAddress)) {
                throw new BaseException();
            } else {
                return musicAddress;
            }
        } catch (Exception e) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
    }
    
    @Override
    public String upload(File srcFile) {
        try {
            String musicAddresses = getMusicAddresses(srcFile.getName(), false);
            if (StringUtils.isNotBlank(musicAddresses)) {
                return srcFile.getName();
            }
        } catch (BaseException e) {
            if (!StringUtils.equals(e.getErrorCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                throw new BaseException(e.getErrorCode(), e.getErrorMsg());
            }
        }
        String loginJwtCache = getLoginJwtCache(config);
        String upload = RequestUtils.upload(config.getHost(), config.getObjectSave().get(0), srcFile, loginJwtCache);
        // 校验是否上传成功
        try {
            getMusicAddresses(srcFile.getName(), true);
        } catch (Exception e) {
            throw new BaseException(ResultCode.OSS_UPLOAD_ERROR);
        }
        return upload;
    }
    
    /**
     * 获取所有音乐下载地址
     *
     * @param md5     音乐的md5值
     * @param refresh 是否刷新缓存
     * @return 音乐下载地址
     */
    @Override
    public Collection<String> getMusicAllMD5(String md5, boolean refresh) {
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
            throw new BaseException(ResultCode.SONG_NOT_EXIST.getCode(), e.getErrorMsg());
        }
    }
    
    @Override
    public boolean delete(String name) {
        throw new BaseException(ResultCode.PERMISSION_NO_ACCESS);
    }
}
