package org.api.admin.model.res.router;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meta {
    private String title;
    private String icon;
    private Integer rank;
    private Boolean showLink;
}