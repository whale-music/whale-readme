package org.api.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveMvReq {
    
    @Schema(title = "id")
    private Long id;
    
    @Schema(title = "标题")
    private String title;
    
    @Schema(title = "视频时长")
    private Long duration;
    
    @Schema(title = "视频介绍")
    private String description;
    
    @Schema(title = "发布时间")
    private LocalDateTime publishTime;
    
    @Schema(title = "MV视频缓存地址")
    private String mvTempPath;
    
    @Schema(title = "封面地址缓存地址")
    private String picTempPath;
    
    @Schema(title = "歌手")
    private List<Long> artistIds;
    
    @Schema(title = "tag")
    private List<String> tags;
}
