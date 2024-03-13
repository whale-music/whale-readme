package org.api.admin.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbResourcePojo;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AudioInfoRes extends TbResourcePojo {
    @Schema(name = "音乐名")
    private String musicName;
    
    @Schema(name = "音乐别名")
    private String aliasName;
    
    @Schema(name = "源文件音乐名")
    private String originFileName;
    
    @Schema(name = "音乐类型")
    private String type;
    
    @Schema(name = "歌手")
    private List<String> artists;
    
    @Schema(name = "专辑")
    private String album;
    
    @Schema(name = "音乐歌词")
    private String lyric;
    
    @Schema(name = "音乐时长")
    private Integer timeLength;
    
    @Schema(name = "歌曲是否存在")
    private Boolean isExist;
    
    @Schema(name = "临时文件名")
    private String musicFileTemp;
}
