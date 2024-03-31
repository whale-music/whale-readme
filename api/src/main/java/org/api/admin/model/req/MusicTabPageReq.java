package org.api.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageReqCommon;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MusicTabPageReq extends PageReqCommon {
    @Schema(name = "all name")
    private String name;
    
    @Schema(name = "音乐名")
    private String musicName;
    
    @Schema(name = "歌手")
    private String artistName;
    
    @Schema(name = "专辑")
    private String albumName;
    
    @Schema(name = "是否刷新缓存")
    private Boolean refresh;
    
    @Schema(name = "是否只显示无音源")
    private Boolean isShowSource;
    
    @Schema(name = "用户ID")
    private Long userId;
}
