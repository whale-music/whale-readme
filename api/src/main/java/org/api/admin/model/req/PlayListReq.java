package org.api.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(name = "歌单tag")
    private List<String> collectTag;
    
    @Schema(name = "分页数据")
    private PageCommon page;
}
