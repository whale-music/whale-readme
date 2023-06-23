package org.api.admin.model.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.PicConvert;
import org.core.mybatis.pojo.TbArtistPojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class MusicInfoReq implements Serializable {
    
    @ApiModelProperty("音乐ID")
    private Long id;
    
    @ApiModelProperty("音乐名")
    private String musicName;
    
    @ApiModelProperty("音乐别名")
    private String musicNameAlias;
    
    @ApiModelProperty("封面")
    private PicConvert pic;
    
    @ApiModelProperty("歌手名ID")
    private List<TbArtistPojo> musicArtist;
    
    @ApiModelProperty("歌手名ID")
    private List<TbArtistPojo> albumArtist;
    
    @ApiModelProperty("专辑")
    private Long albumId;
    
    @ApiModelProperty("歌曲时长")
    private Integer timeLength;
    
    @ApiModelProperty("发行时间")
    private LocalDateTime publishTime;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
