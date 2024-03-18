package org.api.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageCommon;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.PicConvert;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ArtistReq extends ArtistConvert {
    @Schema(name = "封面")
    private PicConvert pic;
    
    @Schema(name = "排序", example = "sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序")
    private String orderBy;
    
    @Schema(name = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order;
    
    @Schema(name = "分页数据")
    private PageCommon page;
}
