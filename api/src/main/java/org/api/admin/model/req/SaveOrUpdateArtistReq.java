package org.api.admin.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.ArtistConvert;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SaveOrUpdateArtistReq extends ArtistConvert {
    String tempFile;
}
