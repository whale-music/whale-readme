package org.api.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusicFileVo {
    
    @ApiModelProperty("音乐ID")
    private String id;
    
    @ApiModelProperty("音乐文件大小")
    private Long size;
    
    @ApiModelProperty("音质")
    private String level;
    
    @ApiModelProperty("文件md5")
    private String md5;
    
    @ApiModelProperty("文件下载地址")
    private String rawUrl;
    
    @ApiModelProperty("文件下载地址")
    private Boolean exists;
}
