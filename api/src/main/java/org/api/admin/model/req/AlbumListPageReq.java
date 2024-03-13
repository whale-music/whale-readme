package org.api.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageCommon;
import org.core.mybatis.model.convert.AlbumConvert;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AlbumListPageReq extends AlbumConvert {
    @Schema(name = "歌手名")
    private String artistName;
    
    @Schema(name = "排序", example = "sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序")
    private String orderBy;
    
    @Schema(name = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order;
    
    @Schema(name = "时间选择", example = "true: 修改时间, false: 上传时间")
    private Boolean timeBy;
    
    @Schema(name = "开始时间")
    private LocalDateTime beforeTime;
    
    @Schema(name = "结束时间")
    private LocalDateTime laterTime;
    
    @Schema(name = "分页数据")
    private PageCommon page;
}
