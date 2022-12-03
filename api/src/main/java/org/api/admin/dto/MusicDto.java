package org.api.admin.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.TbMusicPojo;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MusicDto extends TbMusicPojo {
    @ApiModelProperty("当前页数")
    private Integer pageIndex;
    
    @ApiModelProperty("每页展示行数")
    private Integer pageNum;
}
