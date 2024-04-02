package org.api.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageReqCommon;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AlbumPageReq extends PageReqCommon {
    @Schema(name = "all name")
    private String name;
    
    @Schema(name = "音乐")
    private String musicName;
    
    @Schema(name = "专辑")
    private String albumName;
    
    @Schema(name = "歌手名")
    private String artistName;
    
    @Schema(name = "排序", example = "sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序")
    private String orderBy = "sort";
    
    @Schema(name = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order = false;
    
    @Schema(name = "用户ID")
    private Long userId;
    
    @Schema(name = "开始时间")
    private LocalDateTime beforeTime;
    
    @Schema(name = "结束时间")
    private LocalDateTime laterTime;
}
