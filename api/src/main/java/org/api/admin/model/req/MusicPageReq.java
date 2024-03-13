package org.api.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageCommon;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class MusicPageReq implements Serializable {
    @Schema(name = "歌曲ID列表")
    private List<Long> musicIds;
    
    @Schema(name = "音乐名")
    private String musicName;
    
    @Schema(name = "歌手")
    private String artistName;
    
    @Schema(name = "专辑")
    private String albumName;
    
    @Schema(name = "排序", example = "sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序")
    private String orderBy;
    
    @Schema(name = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order;
    
    @Schema(name = "开始时间")
    private LocalDateTime beforeDate;
    
    @Schema(name = "结束时间")
    private LocalDateTime afterDate;
    
    @Schema(name = "是否刷新缓存")
    private Boolean refresh;
    
    @Schema(name = "是否只显示无音源")
    private Boolean isShowNoExist;
    
    @Schema(name = "分页数据")
    private PageCommon page;
}
