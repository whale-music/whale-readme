package org.api.admin.model.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioInfoReq {
    @ApiModelProperty("音乐ID")
    private Integer id;
    
    @ApiModelProperty("音乐来源")
    @NotBlank
    private String origin;
    
    @ApiModelProperty("音乐名")
    private String musicName;
    
    @ApiModelProperty("音乐别名")
    private List<String> aliaName;
    
    @ApiModelProperty("音乐封面")
    private String pic;
    
    @ApiModelProperty("音乐类型")
    private String type;
    
    @ApiModelProperty("歌手")
    private List<SingerReq> singer;
    
    @ApiModelProperty("专辑")
    private AlbumReq album;
    
    @ApiModelProperty("音乐歌词")
    private String lyric;
    
    @ApiModelProperty("音乐歌词")
    private String kLyric;
    
    @ApiModelProperty("音乐时长")
    private Integer timeLength;
    
    @ApiModelProperty("比特率")
    private Integer rate;
    
    @ApiModelProperty("音乐质量")
    private String level;
    
    @ApiModelProperty("大小")
    private Long size;
    
    @ApiModelProperty("文件md5")
    @NotBlank
    @Length(min = 32, max = 32, message = "MD5长度错误，请重新生成")
    private String md5;
    
    @ApiModelProperty("临时音乐，可以是本地地址，也可以是网络地址")
    @NotBlank
    private String musicTemp;
    
    @ApiModelProperty("true: 只存储到数据库，不上传, false: 读取本地数据上传到数据库")
    private Boolean uploadFlag;
}
