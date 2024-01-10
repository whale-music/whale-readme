package org.api.admin.model.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkAudioResourceReq {
    private Long id;
    private String path;
    @NotNull
    private Long musicId;
}
