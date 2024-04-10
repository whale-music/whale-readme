package org.api.admin.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageReqCommon;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PlaylistMusicPageReq extends PageReqCommon {
    private String id;
    private String musicName;
}
