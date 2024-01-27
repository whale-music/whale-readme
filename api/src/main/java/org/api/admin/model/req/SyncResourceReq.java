package org.api.admin.model.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncResourceReq {
    @NotBlank
    private String type;
    
    @NotBlank
    private String path;
}
