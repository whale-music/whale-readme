package org.core.model.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@ApiModel("保存 音乐下载地址")
public class TbMusicUrlPojoRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 主键
     */
    @NotNull(message = "id can not null")
    @ApiModelProperty("主键")
    private Long id;
    
    
    /**
     * 音乐ID
     */
    @NotNull(message = "musicId can not null")
    @ApiModelProperty("音乐ID")
    private Long musicId;
    
    
    /**
     * 比特率，音频文件的信息
     */
    @ApiModelProperty("比特率，音频文件的信息")
    private Integer rate;
    
    
    /**
     * 音乐地址
     */
    @ApiModelProperty("音乐地址")
    private String url;
    
    
    /**
     * 保存音乐本体的md5，当上传新的音乐时做比较。如果相同则表示已存在
     */
    @ApiModelProperty("保存音乐本体的md5，当上传新的音乐时做比较。如果相同则表示已存在")
    private String md5;
    
    
    /**
     * 音乐质量
     */
    @ApiModelProperty("音乐质量")
    private String level;
    
    
    /**
     * 文件格式类型
     */
    @ApiModelProperty("文件格式类型")
    private String encodeType;
    
    
    /**
     * 文件大小
     */
    @ApiModelProperty("文件大小")
    private Long size;
    
    
    /**
     * 上传用户ID
     */
    @NotNull(message = "userId can not null")
    @ApiModelProperty("上传用户ID")
    private Long userId;
    
    
    /**
     * 音乐来源
     */
    @ApiModelProperty("音乐来源")
    private String origin;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;
    
}
