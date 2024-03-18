package org.api.admin.model.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.utils.MyPageUtil;

import java.io.Serializable;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PageReqCommon implements Serializable {
    @Schema(name = "当前页数")
    private Integer pageIndex;
    
    @Schema(name = "每页展示行数")
    private Integer pageNum;
    
    
    public static PageReqCommon of(Integer pageIndex, Integer pageNum) {
        PageReqCommon common = new PageReqCommon();
        common.setPageIndex(pageIndex);
        common.setPageNum(pageNum);
        return common;
    }
    
    public PageReqCommon getPageCommon() {
        return MyPageUtil.checkPage(this);
    }
    
    public <T> Page<T> getPage() {
        return new Page<>(pageIndex, pageNum);
    }
}
