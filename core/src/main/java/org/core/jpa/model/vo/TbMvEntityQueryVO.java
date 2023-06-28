package org.core.jpa.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("自定义查询 音乐短片")
public class TbMvEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    
    /**
     * 视频资源ID
     */
    @ApiModelProperty("视频资源ID")
    private Long sourceId;
    
    
    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;
    
    
    /**
     * 视频内容简介
     */
    @ApiModelProperty("视频内容简介")
    private String description;
    
    
    /**
     * 视频时长
     */
    @ApiModelProperty("视频时长")
    private Integer duration;
    
    
    /**
     * 上传用户
     */
    @ApiModelProperty("上传用户")
    private Long userId;
    
    
    /**
     * 发布时间
     */
    @ApiModelProperty("发布时间")
    private LocalDateTime publishTime;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    
}
