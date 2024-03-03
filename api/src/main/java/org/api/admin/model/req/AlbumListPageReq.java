package org.api.admin.model.req;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty("歌手名")
    private String artistName;
    
    @ApiModelProperty(value = "排序", example = "sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序")
    private String orderBy;
    
    @ApiModelProperty(value = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order;
    
    @ApiModelProperty(value = "时间选择", example = "true: 修改时间, false: 上传时间")
    private Boolean timeBy;
    
    @ApiModelProperty("开始时间")
    private LocalDateTime beforeTime;
    
    @ApiModelProperty("结束时间")
    private LocalDateTime laterTime;
    
    @ApiModelProperty("分页数据")
    private PageCommon page;
}
