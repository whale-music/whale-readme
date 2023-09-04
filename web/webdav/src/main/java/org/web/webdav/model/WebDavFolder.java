package org.web.webdav.model;

import io.milton.annotations.CreatedDate;
import io.milton.annotations.Name;
import io.milton.annotations.UniqueId;
import io.milton.http.Auth;
import io.milton.http.Request;
import io.milton.http.exceptions.BadRequestException;
import io.milton.http.exceptions.NotAuthorizedException;
import io.milton.resource.Resource;

import java.util.Collection;
import java.util.Date;

/**
 * Webdav 文件夹
 */
public class WebDavFolder implements Resource {
    private String name;
    private Date createdDate;
    private Collection<Resource> list;
    
    public WebDavFolder(String name) {
        this.name = name;
    }
    
    @Override
    public String getUniqueId() {
        return null;
    }
    
    @Name
    @UniqueId
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @CreatedDate
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public Collection<Resource> getList() {
        return list;
    }
    
    public void setList(Collection<Resource> list) {
        this.list = list;
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
    
    @Override
    public Date getModifiedDate() {
        return null;
    }
    
    @Override
    public String checkRedirect(Request request) throws NotAuthorizedException, BadRequestException {
        return null;
    }
    
    @Override
    public String toString() {
        return "WebDavFolder{" +
                "name='" + name + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
