package org.api.admin.model.res.router;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public
class Children {
    private String path;
    private String name;
    private Meta meta;
    private String component;
}