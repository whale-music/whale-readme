package org.oss.service.impl.alist.model.list;

public class MusicListReq {
    private String path;
    private Integer perPage;
    private String password;
    private Boolean refresh;
    private Integer page;
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public Integer getPerPage() {
        return perPage;
    }
    
    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setRefresh(Boolean refresh) {
        this.refresh = refresh;
    }
    
    public Boolean isRefresh() {
        return refresh;
    }
    
    public Integer getPage() {
        return page;
    }
    
    public void setPage(Integer page) {
        this.page = page;
    }
}
