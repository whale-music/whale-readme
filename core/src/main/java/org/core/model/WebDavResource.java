package org.core.model;

import lombok.Data;

import java.net.URLConnection;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * Webdav文件
 */
@Data
public class WebDavResource {
    private String name;
    private String md5;
    private String path;
    private String dbPath;
    private String url;
    private String fileSuffix;
    private Date createdDate;
    private Date modifiedDate;
    private Long contentLength;
    
    private Boolean isFile;
    
    private List<WebDavResource> resource;
    
    
    public WebDavResource() {
    }
    
    
    /**
     * 创建顶层目录下选择文件夹 喜爱歌单, 推荐歌单, 收藏歌单
     *
     * @param name 文件名
     * @param path webdav 网络路径
     */
    public WebDavResource(String name, String path) {
        this.name = name;
        this.path = path;
        this.createdDate = new Date();
        this.modifiedDate = new Date();
        this.contentLength = -1L;
        this.isFile = false;
    }
    
    /**
     * 创建文件
     *
     * @param name          文件名
     * @param md5           md5
     * @param path          webdav 网络路径
     * @param dbPath        数据库路径
     * @param url           文件地址
     * @param createdDate   创建时间
     * @param modifiedDate  修改时间
     * @param contentLength 文件长度
     */
    private WebDavResource(String name, String md5, String path, String dbPath, String url, Date createdDate, Date modifiedDate, Long contentLength) {
        this.name = name;
        this.md5 = md5;
        this.path = path;
        this.dbPath = dbPath;
        this.url = url;
        this.fileSuffix = URLConnection.guessContentTypeFromName(path);
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.contentLength = contentLength;
        this.isFile = true;
        this.resource = Collections.emptyList();
    }
    
    /**
     * 创建文件夹
     *
     * @param name          文件夹
     * @param path          webdav网络路径
     * @param url           文件下载地址
     * @param createdDate   创建时间
     * @param modifiedDate  修改时间
     * @param contentLength 文件长度
     * @param resource      文件夹下数据
     */
    private WebDavResource(String name, String path, String url, Date createdDate, Date modifiedDate, Long contentLength, List<WebDavResource> resource) {
        this.name = name;
        this.path = path;
        this.url = url;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.contentLength = contentLength;
        this.isFile = false;
        this.resource = resource;
    }
    
    public static WebDavResource createRoot(List<WebDavResource> resource) {
        return new WebDavResource("/", "/", "/", new Date(), new Date(), -1L, resource);
    }
    
    public static WebDavResource createFile(String name, String md5, String path, String dbPath, String url, Date createdDate, Date modifiedDate, Long contentLength) {
        return new WebDavResource(name, md5, path, dbPath, url, createdDate, modifiedDate, contentLength);
    }
    
    public static WebDavResource createSelectCollect(String name, String path) {
        return new WebDavResource(name, path);
    }
    
    public static WebDavResource createCollect(String name, String path, Date createdDate, Date modifiedDate, List<WebDavResource> resource) {
        return new WebDavResource(name, path, null, createdDate, modifiedDate, -1L, resource);
    }
    
    public boolean getIsFile() {
        return Boolean.TRUE.equals(isFile);
    }
}
