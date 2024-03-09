package org.api.admin.model.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageCommon;
import org.core.mybatis.pojo.TbCollectPojo;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlayListReq extends TbCollectPojo {
    @ApiModelProperty("歌单tag")
    private List<String> collectTag;
    
    @ApiModelProperty("分页数据")
    private PageCommon page;
}
