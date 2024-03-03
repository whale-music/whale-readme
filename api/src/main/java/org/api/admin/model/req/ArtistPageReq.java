package org.api.admin.model.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageReqCommon;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArtistPageReq extends PageReqCommon {
    @ApiModelProperty("all name")
    private String name;
    
    @ApiModelProperty("音乐名")
    private String musicName;
    
    @ApiModelProperty("歌手")
    private String artistName;
    
    @ApiModelProperty("专辑")
    private String albumName;
}
