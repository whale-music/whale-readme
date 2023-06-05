package org.api.admin.model.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageCommon;
import org.core.model.convert.ArtistConvert;
import org.core.model.convert.PicConvert;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ArtistReq extends ArtistConvert {
    @ApiModelProperty("封面")
    private PicConvert pic;
    
    @ApiModelProperty(value = "排序", example = "sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序")
    private String orderBy;
    
    @ApiModelProperty(value = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order;
    
    @ApiModelProperty("分页数据")
    private PageCommon page;
}
