package org.api.admin.model.req.upload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.core.pojo.TbAlbumPojo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AlbumInfoReq extends TbAlbumPojo {
    @ApiModelProperty("封面")
    private String pic;
    
    @ApiModelProperty("封面")
    private List<String> tags;
}
