package org.web.webdav.tomcat;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.webresources.StandardRoot;

@Slf4j
public class WebdavStandardRoot extends StandardRoot {
    public WebdavStandardRoot(Context context) {
        super(context);
    }
    
    
    @Override
    protected WebResourceSet createMainResourceSet() {
        WebdavResourceSet myDirResourceSet = null;
        for (WebResourceSet preResource : getPreResources()) {
            if (preResource instanceof WebdavResourceSet myDirResourceSet1) {
                myDirResourceSet = myDirResourceSet1;
            }
        }
        if (myDirResourceSet != null) {
            return myDirResourceSet;
        }
        return super.createMainResourceSet();
    }
    
    /**
     * 只创建指定资源, 不读取jar, class等其他目录
     *
     * @param type         The type of {@link WebResourceSet} to create
     * @param webAppMount  The path within the web application that the
     *                     resources should be published at. It must start
     *                     with '/'.
     * @param base         The location of the resources
     * @param archivePath  The path within the resource to the archive where
     *                     the content is to be found. If there is no
     *                     archive then this should be <code>null</code>.
     * @param internalPath The path within the archive (or the resource if the
     *                     archivePath is <code>null</code> where the
     *                     content is to be found. It must start with '/'.
     */
    @Override
    public void createWebResourceSet(ResourceSetType type, String webAppMount, String base, String archivePath, String internalPath) {
        if (type == ResourceSetType.PRE) {
            super.createWebResourceSet(type, webAppMount, base, archivePath, internalPath);
        }
    }
}
