package org.api.admin.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResCommon<T> {
    private Long current;
    private Long size;
    private Long total = 0L;
    private List<T> content;
    
    public PageResCommon(Integer current, Integer size) {
        this.current = Long.valueOf(current);
        this.size = Long.valueOf(size);
    }
    
    public PageResCommon() {
        this.current = 0L;
        this.size = 50L;
    }
    
    public void setCurrent(Long current) {
        this.current = current;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    public void setCurrent(Integer current) {
        this.current = Long.valueOf(current);
    }
    
    public void setSize(Integer size) {
        this.size = Long.valueOf(size);
    }
    
    public void setTotal(Integer total) {
        this.total = Long.valueOf(total);
    }
    
    public void setTotal(Long total) {
        this.total = total;
    }
}
