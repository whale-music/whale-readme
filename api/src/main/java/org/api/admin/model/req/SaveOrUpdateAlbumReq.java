package org.api.admin.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.model.convert.AlbumConvert;
import org.core.model.convert.PicConvert;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SaveOrUpdateAlbumReq extends AlbumConvert {
    private PicConvert picConvert;
    
}
