package org.api.admin.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.TbAlbumPojo;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SaveOrUpdateAlbumReq extends TbAlbumPojo {

}
