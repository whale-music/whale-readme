package org.api.admin.model.res.router;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouterVo {
    private String path;
    private Meta meta;
    private List<Children> children;
}
