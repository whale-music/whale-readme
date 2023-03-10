package org.api.admin.model.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageCommon;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class MusicPageReq implements Serializable {
    
    @ApiModelProperty("音乐名")
    private String musicName;
    
    @ApiModelProperty("歌手")
    private String singerName;
    
    @ApiModelProperty("专辑")
    private String albumName;
    
    @ApiModelProperty(value = "排序", example = "sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序")
    private String orderBy;
    
    @ApiModelProperty(value = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order;
    
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime beforeDate;
    
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime afterDate;
    
    @ApiModelProperty("分页数据")
    PageCommon page;
}
