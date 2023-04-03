package org.api.admin.model.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.AlbumPojo;
import org.core.pojo.ArtistPojo;
import org.core.pojo.MusicPojo;
import org.core.pojo.MusicUrlPojo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MusicRes extends MusicPojo {
    @ApiModelProperty("歌手")
    private List<ArtistPojo> singerList;
    
    @ApiModelProperty("专辑名")
    private AlbumPojo album;
    
    @ApiModelProperty("歌曲地址")
    private List<MusicUrlPojo> musicUrlList;
    
    @ApiModelProperty("是否可以播放")
    private Boolean isPlaying;
}
