package org.api.admin.model.res.router;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public
class Meta {
    private String title;
    private String icon;
    private Integer rank;
    private List<String> auths;
}