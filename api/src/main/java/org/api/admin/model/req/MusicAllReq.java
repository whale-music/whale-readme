package org.api.admin.model.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageCommon;
import org.core.pojo.TbMusicPojo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MusicAllReq extends TbMusicPojo {
    @ApiModelProperty("歌曲ID列表")
    private List<Long> musicIds;
    
    @ApiModelProperty("歌手")
    private String singerName;
    
    @ApiModelProperty("专辑")
    private String albumName;
    
    @ApiModelProperty(value = "排序", example = "sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序")
    private String orderBy;
    
    @ApiModelProperty(value = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order;
    
    PageCommon page;
}
