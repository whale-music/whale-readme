package org.api.model.album;

import java.util.List;

public class ArItem {
    private String name;
    private int id;
    private List<String> alia;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public List<String> getAlia() {
        return alia;
    }
    
    public void setAlia(List<String> alia) {
        this.alia = alia;
    }
    
    @Override
    public String toString() {
        return
                "ArItem{" +
                        "name = '" + name + '\'' +
                        ",id = '" + id + '\'' +
                        ",alia = '" + alia + '\'' +
                        "}";
    }
}