package org.api.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageReqCommon;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PageTagsReq extends PageReqCommon {
    
    @Schema(name = "过滤 tag 类型")
    private List<String> type;
    
    @Schema(name = "tag 索引, tag名的第一个字符的字母")
    private String tagIndex;
    
    @Schema(name = "tag 过滤列表")
    private List<String> filterTagContents;
    
}
