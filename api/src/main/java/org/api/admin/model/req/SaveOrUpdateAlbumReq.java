package org.api.admin.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.AlbumConvert;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SaveOrUpdateAlbumReq extends AlbumConvert {
    private List<Long> artistIds;
    private String tempFile;
}
