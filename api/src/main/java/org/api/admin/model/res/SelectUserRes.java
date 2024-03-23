package org.api.admin.model.res;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SelectUserRes {
    
    private Long value;
    private String label;
    
    public SelectUserRes(Long id, String username) {
        this.value = id;
        this.label = username;
    }
}
