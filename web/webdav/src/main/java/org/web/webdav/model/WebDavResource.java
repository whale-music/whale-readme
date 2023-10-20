package org.web.webdav.model;

import io.milton.annotations.*;
import io.milton.http.Auth;
import io.milton.http.Request;
import io.milton.resource.Resource;
import org.core.mybatis.pojo.TbResourcePojo;

import java.util.Date;
import java.util.List;

/**
 * Webdav文件
 */
public class WebDavResource implements Resource {
    private String name;
    private String md5;
    private String path;
    private List<TbResourcePojo> resource;
    private Date createdDate;
    private Date modifiedDate;
    private Long contentLength;
    
    public WebDavResource(String name, Date createdDate, Date modifiedDate, Long contentLength) {
        this.name = name;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.contentLength = contentLength;
    }
    
    public WebDavResource(String name, String md5, String path, Long contentLength, List<TbResourcePojo> resource) {
        this.name = name;
        this.md5 = md5;
        this.path = path;
        this.contentLength = contentLength;
        this.resource = resource;
    }
    
    public WebDavResource() {
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getMd5() {
        return md5;
    }
    
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    
    @Override
    public String getUniqueId() {
        return getMd5();
    }
    
    @Name
    @UniqueId
    public String getName() {
        return name;
    }
    
    public List<TbResourcePojo> getResource() {
        return resource;
    }
    
    public void setResource(List<TbResourcePojo> resource) {
        this.resource = resource;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public Object authenticate(String user, String password) {
        return null;
    }
    
    @Override
    public boolean authorise(Request request, Request.Method method, Auth auth) {
        return false;
    }
    
    @Override
    public String getRealm() {
        return null;
    }
    
    @ModifiedDate
    public Date getModifiedDate() {
        return modifiedDate;
    }
    
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
    
    @Override
    public String checkRedirect(Request request) {
        return null;
    }
    
    @CreatedDate
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    @ContentLength
    public Long getContentLength() {
        return contentLength;
    }
    
    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }
}
