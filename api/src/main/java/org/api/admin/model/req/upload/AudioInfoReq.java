package org.api.admin.model.req.upload;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.model.convert.PicConvert;
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
    private PicConvert pic;
    
    @ApiModelProperty("音乐类型")
    private String type;
    
    @ApiModelProperty("歌手")
    private List<ArtistInfoReq> artists;
    
    @ApiModelProperty("专辑")
    private AlbumInfoReq album;
    
    @ApiModelProperty("音乐歌词")
    private String lyric;
    
    @ApiModelProperty("逐字歌词")
    private String kLyric;
    
    @ApiModelProperty("未知歌词")
    private String tLyric;
    
    @ApiModelProperty("音乐时长")
    private Integer timeLength;
    
    @ApiModelProperty("比特率")
    private Integer rate;
    
    @ApiModelProperty("音乐质量")
    private String level;
    
    @ApiModelProperty("音乐文件大小")
    private Long size;
    
    @ApiModelProperty("文件md5")
    @NotBlank
    @Length(min = 32, max = 32, message = "MD5长度错误，请重新生成")
    private String md5;
    
    @ApiModelProperty("临时音乐，可以是本地地址，也可以是网络地址, 如果uploadFlag为true，则会把此字段作为相对路径写入数据库")
    @NotBlank
    private String musicTemp;
    
    @ApiModelProperty("true: 只存储到数据库，不上传, false: 读取本地数据或网络数据上传到数据库")
    private Boolean uploadFlag;
    
    @ApiModelProperty("隐藏字段，测试时使用")
    private Long userId;
}
