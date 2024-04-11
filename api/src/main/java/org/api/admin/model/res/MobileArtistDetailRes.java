package org.api.admin.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.MusicConvert;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileArtistDetailRes {
    
    @Schema(title = "歌手ID")
    private Long id;
    
    @Schema(title = "歌手名")
    private String artistName;
    
    @Schema(title = "歌手别名")
    private String aliasName;
    
    @Schema(title = "歌手性别")
    private String sex;
    
    @Schema(title = "出生年月")
    private LocalDate birth;
    
    @Schema(title = "所在国家")
    private String location;
    
    @Schema(title = "歌手介绍")
    private String introduction;
    
    @Schema(title = "上传用户ID")
    private Long userId;
    
    @Schema(name = "封面地址")
    private String picUrl;
    
    private List<MusicConvert> musicList;
    
}
