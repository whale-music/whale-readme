package org.api.admin.model.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.TbSingerPojo;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SingerRes extends TbSingerPojo {
    @ApiModelProperty("专辑数量")
    private String albumSize;
    
    @ApiModelProperty("歌曲数量")
    private String musicSize;
}
