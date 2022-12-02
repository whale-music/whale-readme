package org.api.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioInfoDto {
    @ApiModelProperty("音乐名")
    private String musicName;
    
    @ApiModelProperty("音乐别名")
    private String aliaName;
    
    @ApiModelProperty("音乐封面")
    private String pic;
    
    @ApiModelProperty("音乐类型")
    private String type;
    
    @ApiModelProperty("歌手")
    private List<SingerDto> singer;
    
    @ApiModelProperty("专辑")
    private AlbumDto album;
    
    @ApiModelProperty("音乐歌词")
    private String lyric;
    
    @ApiModelProperty("音乐时长")
    private Integer timeLength;
    
    @ApiModelProperty("音乐质量")
    private String quality;
    
    @ApiModelProperty("比特率")
    private Integer rate;
    
    @ApiModelProperty("大小")
    private Long size;
    
    @ApiModelProperty("临时文件名")
    private String musicFileTemp;
}
