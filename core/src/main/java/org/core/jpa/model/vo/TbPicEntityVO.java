package org.core.jpa.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@ApiModel("保存 音乐专辑歌单封面表")
public class TbPicEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @NotNull(message = "id can not null")
    private Long id;
    
    
    /**
     * 音乐网络地址，或路径
     */
    @NotNull(message = "url can not null")
    @ApiModelProperty("音乐网络地址，或路径")
    private String url;
    
    @NotNull(message = "md5 can not null")
    private String md5;
    
    
    /**
     * 更新时间
     */
    @NotNull(message = "updateTime can not null")
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    
    
    /**
     * 创建时间
     */
    @NotNull(message = "createTime can not null")
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
}
