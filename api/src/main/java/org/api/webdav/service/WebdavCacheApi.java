package org.api.webdav.service;

import org.api.webdav.constant.WebdavCacheConstant;
import org.core.utils.i18n.I18nUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class WebdavCacheApi {
    
    public static String i18RefreshStr() {
        return "/" + I18nUtil.getMsg("webdav.playlist.refresh");
    }
    
    @CacheEvict(value = {
            WebdavCacheConstant.WEBDAV_COLLECT_TYPE_LIST,
            WebdavCacheConstant.WEBDAV_PLAY_LIST,
            WebdavCacheConstant.WEBDAV_USER_POJO,
            WebdavCacheConstant.WEBDAV_RESOURCE
    }, allEntries = true)
    public void refreshAllCache() {
        // refresh webdav all cache
    }
}
