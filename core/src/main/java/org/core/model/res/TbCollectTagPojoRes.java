package org.core.model.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@ApiModel("保存 歌单风格中间表")
public class TbCollectTagPojoRes implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 歌单ID
     */
    @NotNull(message = "collectId can not null")
    @ApiModelProperty("歌单ID")
    private Long collectId;
    
    
    /**
     * tag ID
     */
    @NotNull(message = "tagId can not null")
    @ApiModelProperty("tag ID")
    private Long tagId;
    
}
