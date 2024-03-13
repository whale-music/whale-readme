package org.api.admin.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbArtistPojo;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ArtistRes extends TbArtistPojo {
    @Schema(name = "封面")
    private String picUrl;
    
    
    @Schema(name = "专辑数量")
    private String albumSize;
    
    @Schema(name = "歌曲数量")
    private String musicSize;
}
