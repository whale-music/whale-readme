package org.web.webdav.tomcat;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.WebResource;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.util.ResourceSet;
import org.apache.catalina.webresources.AbstractFileResourceSet;
import org.apache.catalina.webresources.EmptyResource;
import org.api.webdav.service.WebdavCacheApi;
import org.core.model.WebDavResource;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;
import java.util.Set;

/**
 * 用于操作文件或文件夹，获取文件或文件夹信息
 */
@Slf4j
public class WebdavResourceSet extends AbstractFileResourceSet {
    
    private final WebdavCacheApi cacheApi;
    
    public WebdavResourceSet(WebResourceRoot root, String base, WebdavCacheApi cacheApi) {
        super("/");
        setRoot(root);
        setWebAppMount("/");
        setBase(base);
        this.cacheApi = cacheApi;
    }
    
    
    @Override
    public WebResource getResource(String path) {
        checkPath(path);
        log.debug("resource: {}", path);
        WebResourceRoot root = getRoot();
        WebDavResource fileInfo = cacheApi.getResource(path);
        if (fileInfo == null) {
            return new EmptyResource(root, path);
        }
        // 文件只读
        return new DataResource(root, path, fileInfo, false, getManifest());
        
    }
    
    @Override
    public String[] list(String path) {
        checkPath(path);
        WebDavResource cache = cacheApi.getResource(path);
        if (Objects.isNull(cache)) {
            return new String[0];
        }
        return cache.getResource().parallelStream().map(WebDavResource::getPath).toArray(String[]::new);
    }
    
    @Override
    public Set<String> listWebAppPaths(String path) {
        return new ResourceSet<>();
    }
    
    @Override
    public boolean mkdir(String path) {
        return false;
    }
    
    @Override
    public boolean write(String path, InputStream is, boolean overwrite) {
        return false;
    }
    
    @Override
    protected void checkType(File file) {
        if (!file.isDirectory()) {
            throw new IllegalArgumentException(sm.getString("dirResourceSet.notDirectory",
                    getBase(), File.separator, getInternalPath()));
        }
    }
    
}
