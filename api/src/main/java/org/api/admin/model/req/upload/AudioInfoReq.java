package org.api.admin.model.req.upload;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.PicConvert;
import org.core.mybatis.pojo.TbAlbumPojo;
import org.core.mybatis.pojo.TbArtistPojo;
import org.core.mybatis.pojo.TbOriginPojo;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudioInfoReq {
    
    @ApiModelProperty("音乐")
    private AudioMusic music;
    
    @ApiModelProperty("歌手")
    private List<AudioArtist> artists;
    
    @ApiModelProperty("专辑")
    private AudioAlbum album;
    
    @ApiModelProperty("音源")
    private List<AudioSource> sources;
    
    @ApiModelProperty("true: 只存储到数据库，不上传, false: 读取本地数据或网络数据上传到数据库")
    private Boolean uploadFlag;
    
    @ApiModelProperty("隐藏字段，测试时使用")
    private Long userId;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AudioMusic {
        @ApiModelProperty("音乐ID")
        private Integer id;
        
        @ApiModelProperty("音乐名")
        private String musicName;
        
        @ApiModelProperty("音乐别名")
        private List<String> aliaName;
        
        @ApiModelProperty("音乐流派, 逗号分割(英文)")
        private String musicGenre;
        
        @ApiModelProperty("音乐tag, 逗号分割(英文)")
        private String tags;
        
        @ApiModelProperty("音乐封面, 可以是http地址或base64")
        private PicConvert pic;
        
        @ApiModelProperty("音乐歌词")
        private String lyric;
        
        @ApiModelProperty("逐字歌词")
        private String kLyric;
        
        @ApiModelProperty("未知歌词")
        private String tLyric;
        
        @ApiModelProperty("音乐时长")
        private Integer timeLength;
        
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class AudioAlbum extends TbAlbumPojo {
        @ApiModelProperty("专辑流派")
        private String genre;
        
        @ApiModelProperty("专辑封面, 可以是http地址或base64")
        private PicConvert pic;
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class AudioArtist extends TbArtistPojo {
        @ApiModelProperty("艺术家封面, 可以是http地址或base64")
        private PicConvert pic;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AudioSource {
        
        @ApiModelProperty("音乐来源")
        @NotBlank
        private TbOriginPojo origin;
        
        @ApiModelProperty("比特率")
        private Integer rate;
        
        @ApiModelProperty("音乐质量")
        private String level;
        
        @ApiModelProperty("音乐类型")
        private String encodeType;
        
        @ApiModelProperty("音乐文件大小")
        private Long size;
        
        @ApiModelProperty("文件md5")
        @NotBlank
        @Length(min = 32, max = 32, message = "MD5长度错误，请重新生成")
        private String md5;
        
        @ApiModelProperty("临时音乐，可以是本地地址，也可以是网络地址, 如果uploadFlag为true，则会把此字段作为相对路径写入数据库")
        @NotBlank
        private String pathTemp;
    }
}
