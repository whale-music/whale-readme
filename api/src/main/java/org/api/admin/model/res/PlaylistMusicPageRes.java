package org.api.admin.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistMusicPageRes {
    
    @Schema(name = "音乐ID")
    private Long id;
    
    @Schema(name = "音乐名")
    private String musicName;
    
    @Schema(name = "音乐别名")
    private String musicNameAlias;
    
    @Schema(name = "封面")
    private String picUrl;
    
    @Schema(name = "歌手名ID")
    private List<Long> artistIds;
    
    @Schema(name = "歌手名")
    private List<String> artistNames;
    
    @Schema(name = "专辑")
    private Long albumId;
    
    @Schema(name = "音乐是否存在")
    private Boolean isExist;
    
    @Schema(name = "音乐是否喜欢")
    private Boolean isLike;
}
