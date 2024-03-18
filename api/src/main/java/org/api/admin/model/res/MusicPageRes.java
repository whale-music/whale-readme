package org.api.admin.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class MusicPageRes implements Serializable {
    
    @Schema(name = "音乐ID")
    private Long id;
    
    @Schema(name = "音乐名")
    private String musicName;
    
    @Schema(name = "音乐别名")
    private String musicNameAlias;
    
    @Schema(name = "封面")
    private String pic;
    
    @Schema(name = "歌手名ID")
    private List<Long> artistIds;
    
    @Schema(name = "歌手名")
    private List<String> artistNames;
    
    @Schema(name = "专辑")
    private Long albumId;
    
    @Schema(name = "专辑")
    private String albumName;
    
    @Schema(name = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order;
    
    @Schema(name = "音乐是否存在")
    private Boolean isExist;
    
    @Schema(name = "音乐是否喜欢")
    private Boolean isLike;
    
    @Schema(name = "音乐地址")
    private String musicRawUrl;
    
    @Schema(name = "歌曲时长")
    private Integer timeLength;
    
    @Schema(name = "发行时间")
    private LocalDateTime publishTime;
    
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
}
