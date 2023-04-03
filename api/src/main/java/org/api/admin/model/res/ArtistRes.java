package org.api.admin.model.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.ArtistPojo;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ArtistRes extends ArtistPojo {
    @ApiModelProperty("专辑数量")
    private String albumSize;
    
    @ApiModelProperty("歌曲数量")
    private String musicSize;
}
