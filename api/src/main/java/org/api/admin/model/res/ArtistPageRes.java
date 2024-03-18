package org.api.admin.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistPageRes {
    
    @Schema(title = "歌手ID")
    private Long id;
    
    @Schema(title = "歌手名")
    private String artistName;
    
    @Schema(title = "歌手别名")
    private String aliasName;
    
    @Schema(title = "上传用户ID")
    private Long userId;
    
    @Schema(name = "封面")
    private String picUrl;
    
    @Schema(name = "专辑数量")
    private String albumSize;
    
    @Schema(name = "歌曲数量")
    private String musicSize;
    
    @Schema(title = "创建时间")
    private LocalDateTime createTime;
}
