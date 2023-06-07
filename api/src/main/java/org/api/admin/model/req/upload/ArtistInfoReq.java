package org.api.admin.model.req.upload;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.core.model.convert.PicConvert;
import org.core.pojo.TbArtistPojo;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArtistInfoReq extends TbArtistPojo {
    @ApiModelProperty("封面")
    private PicConvert pic;
}
