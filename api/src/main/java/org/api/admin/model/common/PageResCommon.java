package org.api.admin.model.common;

import java.util.List;


public class PageResCommon<T> {
    private Long current;
    private Long size;
    private Long total = 0L;
    private List<T> content;
    
    public PageResCommon(Integer current, Integer size) {
        this.current = Long.valueOf(current);
        this.size = Long.valueOf(size);
    }
    
    public PageResCommon(Integer current, Integer size, Long total, List<T> content) {
        this.current = Long.valueOf(current);
        this.size = Long.valueOf(size);
        this.total = total;
        this.content = content;
    }
    
    public PageResCommon(Long current, Long size, Long total, List<T> content) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.content = content;
    }
    
    public PageResCommon() {
        this.current = 1L;
        this.size = 50L;
    }
    
    public Long getCurrent() {
        return current;
    }
    
    public void setCurrent(Long current) {
        this.current = current;
    }
    
    public Long getSize() {
        return size;
    }
    
    public void setSize(Long size) {
        this.size = size;
    }
    
    public Long getTotal() {
        return total;
    }
    
    public void setTotal(Long total) {
        this.total = total;
    }
    
    public List<T> getContent() {
        return content;
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
    
    public void setContent(List<T> content) {
        this.content = content;
    }
}
