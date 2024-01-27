package org.api.admin.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CleanResourceReq {
    private String type;
    private String picType;
    private Long id;
    private Long middleId;
    private Boolean isForceDelete = false;
}
