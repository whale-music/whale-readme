package org.api.admin.model.req.upload;

import io.swagger.v3.oas.annotations.media.Schema;
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
    
    @Schema(name = "音乐")
    private AudioMusic music;
    
    @Schema(name = "歌手")
    private List<AudioArtist> artists;
    
    @Schema(name = "专辑")
    private AudioAlbum album;
    
    @Schema(name = "音源")
    private List<AudioSource> sources;
    
    @Schema(name = "true: 只存储到数据库，不上传, false: 读取本地数据或网络数据上传到数据库")
    private Boolean uploadFlag;
    
    @Schema(name = "隐藏字段，测试时使用")
    private Long userId;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AudioMusic {
        @Schema(name = "音乐ID")
        private Integer id;
        
        @Schema(name = "音乐名")
        private String musicName;
        
        @Schema(name = "音乐别名")
        private List<String> aliaName;
        
        @Schema(name = "音乐流派, 逗号分割(英文)")
        private String musicGenre;
        
        @Schema(name = "音乐tag, 逗号分割(英文)")
        private String tags;
        
        @Schema(name = "音乐封面, 可以是http地址或base64")
        private PicConvert pic;
        
        @Schema(name = "音乐歌词")
        private String lyric;
        
        @Schema(name = "逐字歌词")
        private String kLyric;
        
        @Schema(name = "未知歌词")
        private String tLyric;
        
        @Schema(name = "音乐时长")
        private Integer timeLength;
        
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class AudioAlbum extends TbAlbumPojo {
        @Schema(name = "专辑流派")
        private String genre;
        
        @Schema(name = "专辑封面, 可以是http地址或base64")
        private PicConvert pic;
    }
    
    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class AudioArtist extends TbArtistPojo {
        @Schema(name = "艺术家封面, 可以是http地址或base64")
        private PicConvert pic;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AudioSource {
        
        @Schema(name = "音乐来源")
        @NotBlank
        private TbOriginPojo origin;
        
        @Schema(name = "比特率")
        private Integer rate;
        
        @Schema(name = "音乐质量")
        private String level;
        
        @Schema(name = "音乐类型")
        private String encodeType;
        
        @Schema(name = "音乐文件大小")
        private Long size;
        
        @Schema(name = "文件md5")
        @NotBlank
        @Length(min = 32, max = 32, message = "MD5长度错误，请重新生成")
        private String md5;
        
        @Schema(name = "临时音乐，可以是本地地址，也可以是网络地址, 如果uploadFlag为true，则会把此字段作为相对路径写入数据库")
        @NotBlank
        private String pathTemp;
    }
}
