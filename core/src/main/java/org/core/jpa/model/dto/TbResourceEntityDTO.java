package org.core.jpa.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "存储地址")
public class TbResourceEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键
     */
    @Schema(name = "主键")
    private Long id;
    
    
    /**
     * 音乐ID
     */
    @Schema(name = "音乐ID")
    private Long musicId;
    
    
    /**
     * 比特率，音频文件的信息
     */
    @Schema(name = "比特率，音频文件的信息")
    private Integer rate;
    
    
    /**
     * 音乐地址, 存储相对路径
     */
    @Schema(name = "音乐地址, 存储相对路径")
    private String path;
    
    
    /**
     * 保存音乐本体的md5，当上传新的音乐时做比较。如果相同则表示已存在
     */
    @Schema(name = "保存音乐本体的md5，当上传新的音乐时做比较。如果相同则表示已存在")
    private String md5;
    
    
    /**
     * 音乐质量
     */
    @Schema(name = "音乐质量")
    private String level;
    
    
    /**
     * 文件格式类型
     */
    @Schema(name = "文件格式类型")
    private String encodeType;
    
    
    /**
     * 文件大小
     */
    @Schema(name = "文件大小")
    private Long size;
    
    
    /**
     * 上传用户ID
     */
    @Schema(name = "上传用户ID")
    private Long userId;
    
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 修改时间
     */
    @Schema(name = "修改时间")
    private LocalDateTime updateTime;
    
}
