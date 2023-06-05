package org.oss.service.impl.alist.model.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentItem {
    private Integer size;
    private String thumb;
    private Boolean isDir;
    private String name;
    private String sign;
    private String modified;
    private Integer type;
    private String path;
}
