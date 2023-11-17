package org.api.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbMvPojo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SaveMvReq extends TbMvPojo {
    @Schema(title = "MV视频缓存地址")
    private String mvTempPath;
    
    @Schema(title = "封面地址缓存地址")
    private String picTempPath;
    
    @Schema(title = "歌手")
    private List<Long> artistIds;
    
    @Schema(title = "tag")
    private List<String> tags;
}
