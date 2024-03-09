package org.api.admin.model.req;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePlayListReq {
    
    @Schema(title = "歌单表ID")
    private Long id;
    
    @Schema(title = "歌单名（包括用户喜爱歌单）")
    private String playListName;
    
    @Schema(title = "歌单类型，0为普通歌单，1为用户喜爱歌单，2为推荐歌单")
    private Byte type;
    
    @Schema(title = "简介")
    private String description;
    
    @ApiModelProperty("歌单tag")
    private List<String> collectTag;
}
