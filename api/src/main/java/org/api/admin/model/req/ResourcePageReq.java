package org.api.admin.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourcePageReq {
    private String select;
    private String order;
    private String orderBy = "";
    private List<String> filter;
    private Boolean filterType;
    private Boolean refresh = false;
}
