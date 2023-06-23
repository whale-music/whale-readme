package org.api.admin.model.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbAlbumPojo;
import org.core.mybatis.pojo.TbArtistPojo;
import org.core.mybatis.pojo.TbMusicPojo;
import org.core.mybatis.pojo.TbMusicUrlPojo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MusicRes extends TbMusicPojo {
    @ApiModelProperty("歌手")
    private List<TbArtistPojo> singerList;
    
    @ApiModelProperty("专辑名")
    private TbAlbumPojo album;
    
    @ApiModelProperty("歌曲地址")
    private List<TbMusicUrlPojo> musicUrlList;
    
    @ApiModelProperty("是否可以播放")
    private Boolean isPlaying;
}
