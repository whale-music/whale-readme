package org.api.admin.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkVideoResourceReq {
    private Long mvId;
    private String path;
}
