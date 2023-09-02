package org.api.admin.model.res.router;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouterVo {
    private String name;
    private String path;
    private String component;
    private String redirect;
    private Meta meta;
    private List<Children> children;
}
