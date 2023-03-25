package org.oss.service.impl.alist;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.oss.service.OSSService;
import org.oss.service.impl.alist.model.list.ContentItem;
import org.oss.service.impl.alist.util.Request;

import java.util.List;

public class AListOSSServiceImpl implements OSSService {
    
    private static final String SERVICE_NAME = "AList";
    
    // 创建缓存，默认4毫秒过期
    TimedCache<String, ContentItem> timedCache = CacheUtil.newTimedCache(1000L * 60L * 60L);
    
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
        return false;
    }
    
    @Override
    public void isExist(String host, String objectSaveConfig, String file) {
        getMusicAddresses(host, objectSaveConfig, file);
    }
    
    @Override
    public String getMusicAddresses(String host, String objectSave, String path) {
        return getCacheMusicAddress(host, objectSave, path);
    }
    
    public String getCacheMusicAddress(String host, String objectSave, String path) {
        try {
            ContentItem item = timedCache.get(path);
            // 获取所有文件保存到缓存中
            if (item == null) {
                List<ContentItem> list = Request.list(host, objectSave);
                for (ContentItem contentItem : list) {
                    timedCache.put(contentItem.getName(), contentItem);
                }
                item = timedCache.get(path);
            }
            // 没有找到文件直接抛出异常
            if (item == null) {
                throw new BaseException(ResultCode.DATA_NONE);
            }
            String musicAddress = String.format("%s/d/%s/%s?sign=%s", host, objectSave, path, item.getSign());
            if (StringUtils.isBlank(musicAddress)) {
                throw new BaseException();
            } else {
                return musicAddress;
            }
        } catch (Exception e) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
    }
    
    public String getMonoMusicAddress(String host, String objectSave, String path) {
        try {
            String sign = Request.getMusicAddress(host, objectSave, path);
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
    public String upload(String objectSaveConfig, String filePath) {
        throw new BaseException(ResultCode.PERMISSION_NO_ACCESS);
    }
    
    @Override
    public boolean delete(String filePath) {
        throw new BaseException(ResultCode.PERMISSION_NO_ACCESS);
    }
}
