package org.api.admin.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageCommon;
import org.core.pojo.TbAlbumPojo;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AlbumReq extends TbAlbumPojo {
    PageCommon page;
}
