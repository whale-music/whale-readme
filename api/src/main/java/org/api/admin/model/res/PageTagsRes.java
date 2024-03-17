package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageTagsRes {
    private Long id;
    private String name;
    private List<String> tagContent;
    private List<String> genres;
    private Set<String> tagType;
    private String tagLinkType;
    private String picUrl;
}
