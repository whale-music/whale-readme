package org.core.oss.service.impl.alist.model.remove.req;

import com.alibaba.fastjson2.annotation.JSONField;

import java.util.List;

public class Remove {
    
    @JSONField(name = "names")
    private List<String> names;
    
    @JSONField(name = "dir")
    private String dir;
    
    public List<String> getNames() {
        return names;
    }
    
    public void setNames(List<String> names) {
        this.names = names;
    }
    
    public String getDir() {
        return dir;
    }
    
    public void setDir(String dir) {
        this.dir = dir;
    }
    
    @Override
    public String toString() {
        return
                "Remove{" +
                        "names = '" + names + '\'' +
                        ",dir = '" + dir + '\'' +
                        "}";
    }
}