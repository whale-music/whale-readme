package org.core.oss.service.impl.local.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileMetadata {
    
    /**
     * 本地绝对路径
     */
    private String fullPath;
    
    /**
     * 文件路径
     */
    private String uri;
    
    /**
     * 文件名
     */
    private String name;
    
    /**
     * 文件大小（字节）
     */
    private long size;
    
    /**
     * 创建时间
     */
    private Date creationTime;
    
    /**
     * 修改时间
     */
    private Date modificationTime;
    
    /**
     * 访问时间
     */
    private Date lastAccessTime;
    
    /**
     * 文件扩展名
     */
    private String fileExtension;
    
    /**
     * 文件md5
     */
    private String fileMd5;
    
}
