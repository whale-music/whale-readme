package org.core.model.req;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("歌单风格中间表")
public class TbCollectTagPojoReq implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 歌单ID
     */
    @ApiModelProperty("歌单ID")
    private Long collectId;
    
    
    /**
     * tag ID
     */
    @ApiModelProperty("tag ID")
    private Long tagId;
    
}
