package org.api.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbMusicPojo;
import org.core.pojo.TbMusicUrlPojo;
import org.core.pojo.TbSingerPojo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MusicVo extends TbMusicPojo {
    @ApiModelProperty("歌手")
    private List<TbSingerPojo> singerList;
    
    @ApiModelProperty("专辑名")
    private TbAlbumPojo album;
    
    @ApiModelProperty("歌曲地址")
    private List<TbMusicUrlPojo> musicUrlList;
    
    @ApiModelProperty("是否可以播放")
    private Boolean isPlaying;
}
