package org.core.utils.http;

import org.core.config.HttpRequestConfig;
import org.core.oss.model.Resource;

import java.io.File;

public class HttpUtil {
    private HttpUtil() {
    }
    
    public static File downloadResource(String url, File file, int timeout) {
        cn.hutool.http.HttpUtil.downloadFile(url, file, timeout);
        return file;
    }
    
    public static File downloadResource(Resource resource, HttpRequestConfig config) {
        File file = config.getTempPathFile(resource.getName());
        return downloadResource(resource.getUrl(), file, config.getTimeout());
    }
    
}
