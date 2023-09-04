package org.web.webdav.controller;

import io.milton.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.web.webdav.model.WebDavFolder;
import org.web.webdav.model.WebDavResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@ResourceController
@Slf4j
public class WebDavController {
    
    /**
     * 获取Webdav根目录
     *
     * @return this
     */
    @Root
    public WebDavController getRoot() {
        return this;
    }
    
    @ChildrenOf
    public List<WebDavFolder> getWebDavFolders(WebDavController root) {
        ArrayList<WebDavFolder> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new WebDavFolder(String.valueOf(i)));
        }
        return list;
    }
    
    @ChildrenOf
    public Collection<WebDavResource> getWebDavFiles(WebDavFolder webDavFolder) {
        ArrayList<WebDavResource> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String name = i + ".txt";
            list.add(new WebDavResource(name, new Date(), new Date(), Long.parseLong(String.valueOf(String.valueOf(i).length()))));
        }
        return list;
    }
    
    @Get
    public InputStream getChild(WebDavResource webDavFolder) {
        String name = webDavFolder.getName();
        log.info("output file: {}", name);
        return new ByteArrayInputStream(name.getBytes());
    }
    
    
    @Name
    public String getWebDavResource(WebDavResource webDavResource) {
        String name = webDavResource.getName();
        log.info("文件名: {}", name);
        return name;
    }
    
    @DisplayName
    public String getDisplayName(WebDavResource webDavResource) {
        return webDavResource.getName();
    }
    
    @UniqueId
    public String getUniqueId(WebDavResource webDavResource) {
        return webDavResource.getName();
    }
    
    @ModifiedDate
    public Date getModifiedDate(WebDavResource webDavResource) {
        Date modifiedDate = webDavResource.getModifiedDate();
        log.info("文件修改时间: {}", modifiedDate);
        return modifiedDate;
    }
    
    @CreatedDate
    public Date getCreatedDate(WebDavResource webDavResource) {
        Date createdDate = webDavResource.getCreatedDate();
        log.info("文件修改时间: {}", createdDate);
        return createdDate;
    }
    
}
