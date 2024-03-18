package org.api.admin.model.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PageCommon implements Serializable {
    @Schema(name = "当前页数")
    private Integer pageIndex;
    
    @Schema(name = "每页展示行数")
    private Integer pageNum;
    
    @Schema(name = "总页数")
    private Integer total;
    
    public static PageCommon of(Integer pageIndex, Integer pageNum, Integer total) {
        return new PageCommon(pageIndex, pageNum, total);
    }
    
    public static PageCommon of(Integer pageIndex, Integer pageNum) {
        PageCommon common = new PageCommon();
        common.setPageIndex(pageIndex);
        common.setPageNum(pageNum);
        return common;
    }
}
