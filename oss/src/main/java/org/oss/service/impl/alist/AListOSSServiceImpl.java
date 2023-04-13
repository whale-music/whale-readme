package org.oss.service.impl.alist;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import com.sun.net.httpserver.Headers;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.jetbrains.annotations.Nullable;
import org.oss.service.OSSService;
import org.oss.service.impl.alist.model.list.ContentItem;
import org.oss.service.impl.alist.util.RequestUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

public class AListOSSServiceImpl implements OSSService {
    
    private static final String SERVICE_NAME = "AList";
    
    private static final String LOGIN_KEY = "loginKey";
    // 创建缓存，默认4毫秒过期
    public static final TimedCache<String, ContentItem> musicUrltimedCache = CacheUtil.newTimedCache(1000L * 60L * 60L);
    // 创建缓存，默认4毫秒过期
    public static final TimedCache<String, String> loginTimeCache = CacheUtil.newTimedCache(1000L * 60L * 60L);
    private String accessKey = "";
    private String secretKey = "";
    
    @Override
    public boolean isCurrentOSS(String serviceName) {
        return StringUtils.equals(SERVICE_NAME, serviceName);
    }
    
    @Override
    public String getMode() {
        return SERVICE_NAME;
    }
    
    @Override
    public boolean isConnected(String host, String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        String loginCacheStr = loginTimeCache.get(LOGIN_KEY);
        if (StringUtils.isBlank(loginCacheStr)) {
            String login = login(host, this.accessKey, this.secretKey);
            loginTimeCache.put(LOGIN_KEY, login);
            return StringUtils.isNotBlank(loginTimeCache.get(LOGIN_KEY));
        }
        return true;
    }
    
    @Override
    public void isExist(String host, String objectSaveConfig, String file) {
        getMusicAddresses(host, objectSaveConfig, file, false);
    }
    
    @Override
    public String getMusicAddresses(String host, String objectSave, String musicFlag, boolean refresh) {
        try {
            String loginCacheStr = getLoginJwtCache(host);
            // 音乐地址URL缓存
            ContentItem item = musicUrltimedCache.get(musicFlag);
            // 没有地址便刷新缓存,获取所有文件保存到缓存中
            // 第一次执行，必须刷新缓存。所以添加添加缓存是否存在条件
            if ((item == null && refresh) || musicUrltimedCache.isEmpty()) {
                Headers headers = new Headers();
                headers.put("Authorization", Collections.singletonList(loginCacheStr));
                List<ContentItem> list = RequestUtils.list(host, objectSave, headers);
                for (ContentItem contentItem : list) {
                    musicUrltimedCache.put(contentItem.getName(), contentItem);
                }
                item = musicUrltimedCache.get(musicFlag);
            }
            // 没有找到文件直接抛出异常
            if (item == null) {
                throw new BaseException(ResultCode.DATA_NONE);
            }
            String musicAddress = String.format("%s/d/%s/%s?sign=%s", host, objectSave, musicFlag, item.getSign());
            if (StringUtils.isBlank(musicAddress)) {
                throw new BaseException();
            } else {
                return musicAddress;
            }
        } catch (Exception e) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
    }
    
    private String login(String host, String accessKey, String secretKey) {
        return RequestUtils.login(host, accessKey, secretKey);
    }
    
    @Nullable
    private String getLoginJwtCache(String host) {
        String loginCacheStr = loginTimeCache.get(LOGIN_KEY);
        if (StringUtils.isBlank(loginCacheStr)) {
            boolean connected = isConnected(host, this.accessKey, this.secretKey);
            if (!connected) {
                throw new BaseException(ResultCode.OSS_LOGIN_ERROR);
            }
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
    public String upload(String host, String objectSaveConfig, File srcFile) {
        String loginJwtCache = getLoginJwtCache(host);
        String upload = RequestUtils.upload(host, objectSaveConfig, srcFile, loginJwtCache);
        try {
            getMusicAddresses(host, objectSaveConfig, srcFile.getName(), false);
        } catch (Exception e) {
            throw new BaseException(ResultCode.OSS_UPLOAD_ERROR);
        }
        return upload;
    }
    
    @Override
    public boolean delete(String host, String objectSaveConfig, String filePath) {
        throw new BaseException(ResultCode.PERMISSION_NO_ACCESS);
    }
}
