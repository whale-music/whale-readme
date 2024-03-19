package org.web.webdav.tomcat;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.webresources.AbstractResource;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.core.model.WebDavResource;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.jar.Manifest;

/**
 * 用于Webdav获取文件信息工具
 */
public class DataResource extends AbstractResource {
    private static final Log log = LogFactory.getLog(DataResource.class);
    
    private final WebDavResource resource;
    private final Boolean readOnly;
    private final Manifest manifest;
    
    public DataResource(WebResourceRoot root, String webAppPath, WebDavResource resource, boolean readOnly, Manifest manifest) {
        super(root, webAppPath);
        this.resource = resource;
        this.readOnly = readOnly;
        this.manifest = manifest;
        this.setMimeType(resource.getFileSuffix());
    }
    
    @Override
    public long getLastModified() {
        if (resource.getModifiedDate() != null) {
            return resource.getModifiedDate().getTime();
        }
        return 0;
    }
    
    @Override
    public boolean exists() {
        return true;
    }
    
    /**
     * 指示应用程序是否需要此资源才能正确扫描文件结构，但 main 或任何其他 WebResourceSet.
     * 例如，如果外部目录映射到空 Web 应用程序中的 /WEB-INF/lib，则 /WEB-INF 将表示为虚拟资源。
     *
     * @return 返回 <code>true</code> 默认为虚拟资源
     */
    @Override
    public boolean isVirtual() {
        return false;
    }
    
    @Override
    public boolean isDirectory() {
        return !isFile();
    }
    
    @Override
    public boolean isFile() {
        return Boolean.TRUE.equals(resource.getIsFile());
    }
    
    @Override
    public boolean delete() {
        return false;
    }
    
    @Override
    public String getName() {
        return resource.getName();
    }
    
    @Override
    public long getContentLength() {
        return getContentLengthInternal();
    }
    
    private long getContentLengthInternal() {
        if (isDirectory()) {
            return -1;
        }
        return resource.getContentLength();
    }
    
    @Override
    public String getCanonicalPath() {
        return resource.getPath();
    }
    
    @Override
    public boolean canRead() {
        return readOnly;
    }
    
    @Override
    protected InputStream doGetInputStream() {
        try (HttpResponse execute = HttpUtil.createGet(resource.getUrl()).execute()) {
            return execute.bodyStream();
        }
    }
    
    @Override
    public final byte[] getContent() {
        return HttpUtil.downloadBytes(resource.getUrl());
    }
    
    
    @Override
    public long getCreation() {
        return getLastModified();
    }
    
    @Override
    public URL getURL() {
        try {
            return URI.create(resource.getUrl()).toURL();
        } catch (MalformedURLException e) {
            if (log.isDebugEnabled()) {
                log.debug(sm.getString("fileResource.getUrlFail", resource.getPath()), e);
            }
            return null;
        }
    }
    
    @Override
    public URL getCodeBase() {
        return getURL();
    }
    
    
    /**
     * @return 用于对此资源进行签名以验证该资源的证书，如果没有，则为null
     */
    @Override
    public Certificate[] getCertificates() {
        return new Certificate[]{};
    }
    
    @Override
    public Manifest getManifest() {
        return manifest;
    }
    
    @Override
    protected Log getLog() {
        return log;
    }
}
