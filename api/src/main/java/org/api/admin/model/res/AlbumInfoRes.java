package org.api.admin.model.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.MusicConvert;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AlbumInfoRes extends AlbumConvert {
    @ApiModelProperty("专辑流派")
    private String albumGenre;
    
    @ApiModelProperty("音乐数据")
    private List<MusicConvert> musicList;
    
    @ApiModelProperty("专辑歌曲数量")
    private Long albumSize;
    
    @ApiModelProperty("歌手信息")
    private List<ArtistConvert> artistList;
}
