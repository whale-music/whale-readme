package org.api.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "all name")
    private String name;
    
    @Schema(name = "音乐名")
    private String musicName;
    
    @Schema(name = "歌手")
    private String artistName;
    
    @Schema(name = "专辑")
    private String albumName;
}
