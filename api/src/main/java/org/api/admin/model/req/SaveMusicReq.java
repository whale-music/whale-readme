package org.api.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbResourcePojo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveMusicReq {
    
    @Schema(title = "音乐ID")
    private Long id;
    
    @Schema(title = "音乐名")
    private String musicName;
    
    @Schema(title = "歌曲别名，数组则使用逗号分割")
    private String aliasName;
    
    @Schema(title = "专辑ID")
    private Long albumId;
    
    @Schema(title = "歌手ID")
    private List<Long> artistIds;
    
    @Schema(title = "上传用户ID")
    private Long userId;
    
    @Schema(title = "歌曲时长")
    private Integer timeLength;
    
    @Schema(title = "音乐描述")
    private String comment;
    
    @Schema(title = "音乐语种")
    private String language;
    
    @Schema(title = "歌曲发布时间")
    private LocalDateTime publishTime;
    
    
    @Schema(name = "音乐tag")
    private Set<String> musicTag = new HashSet<>();
    
    @Schema(name = "音乐流派")
    private Set<String> musicGenre = new HashSet<>();
    
    @Schema(name = "音源信息")
    private TbResourcePojo resource;
    
    @Schema(name = "音源临时地址")
    private String tempMusicFile;
    
    @Schema(name = "封面地址")
    private String tempPicFile;
}
