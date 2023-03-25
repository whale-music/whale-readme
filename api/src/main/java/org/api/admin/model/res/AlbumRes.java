package org.api.admin.model.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbArtistPojo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AlbumRes extends TbAlbumPojo {
    @ApiModelProperty("专辑歌曲数量")
    private Long albumSize;
    
    @ApiModelProperty("歌手信息")
    private List<TbArtistPojo> singer;
    
    @ApiModelProperty(value = "排序", example = "sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序")
    private String orderBy;
    
    @ApiModelProperty(value = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order;
}
