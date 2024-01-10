package org.api.admin.model.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.LinkResourceCommon;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkPicResourceReq {
    @NotNull
    private Long id;
    
    private List<LinkResourceCommon> linkList;
}
