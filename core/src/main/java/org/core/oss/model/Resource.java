package org.core.oss.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
    /**
     * 文件名
     */
    private String name;
    /**
     * 路径
     */
    private String path;
    /**
     * 文件地址
     */
    private String url;
    /**
     * 文件大小（字节）
     */
    private Long size;
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
    private String md5;
}
