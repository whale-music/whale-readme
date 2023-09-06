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
    private String uniqueId;
    private String name;
    private Date createdDate;
    private Collection<? extends Resource> resources;
    
    private Collection<? extends Resource> folders;
    
    public WebDavFolder(String name) {
        this.uniqueId = name;
        this.name = name;
    }
    
    public WebDavFolder(String uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }
    
    public WebDavFolder(String uniqueId, String name, Collection<? extends Resource> folders, Collection<? extends Resource> resources) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.folders = folders;
        this.resources = resources;
    }
    
    @UniqueId
    @Override
    public String getUniqueId() {
        return uniqueId;
    }
    
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
    
    public Collection<? extends Resource> getFolders() {
        return folders;
    }
    
    public void setFolders(Collection<? extends Resource> folders) {
        this.folders = folders;
    }
    
    @Name
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
    
    public Collection<? extends Resource> getResources() {
        return resources;
    }
    
    public void setResources(Collection<Resource> resources) {
        this.resources = resources;
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
