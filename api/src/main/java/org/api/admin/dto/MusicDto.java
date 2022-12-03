package org.api.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.TbMusicPojo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MusicDto extends TbMusicPojo {
    @ApiModelProperty("歌曲ID列表")
    private List<Long> ids;
    
    @ApiModelProperty("歌手")
    private String singerName;
    
    @ApiModelProperty("专辑")
    private String albumName;
    
    @ApiModelProperty(value = "排序", example = "sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序")
    private String orderBy;
    
    @ApiModelProperty("数据顺序")
    private Boolean order;
    
    @ApiModelProperty("当前页数")
    private Integer pageIndex;
    
    @ApiModelProperty("每页展示行数")
    private Integer pageNum;
}
