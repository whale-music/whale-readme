package org.api.admin.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioInfoVo {
    @ApiModelProperty("音乐名")
    private String musicName;
    
    @ApiModelProperty("音乐别名")
    private String aliaName;
    
    @ApiModelProperty("源文件音乐名")
    private String originFileName;
    
    @ApiModelProperty("音乐类型")
    private String type;
    
    @ApiModelProperty("歌手")
    private List<String> singer;
    
    @ApiModelProperty("专辑")
    private String album;
    
    @ApiModelProperty("音乐歌词")
    private String lyric;
    
    @ApiModelProperty("音乐时长")
    private Integer timeLength;
    
    @ApiModelProperty("音乐质量")
    private String quality;
    
    @ApiModelProperty("大小")
    private Long size;
    
    @ApiModelProperty("歌曲是否存在")
    private Boolean isExist;
    
    @ApiModelProperty("临时文件名")
    private String musicFileTemp;
}
