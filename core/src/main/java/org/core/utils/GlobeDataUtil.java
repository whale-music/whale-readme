package org.core.utils;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.date.DateUnit;

public class GlobeDataUtil {
    
    private static final TimedCache<String, String> DATA = CacheUtil.newTimedCache(DateUnit.HOUR.getMillis());
    
    private GlobeDataUtil() {
    }
    
    public static String getData(String key) {
        return DATA.get(key);
    }
    
    public static void setData(String key, String value) {
        DATA.put(key, value);
    }
    
    public static void remove(String key) {
        DATA.remove(key);
    }
}
