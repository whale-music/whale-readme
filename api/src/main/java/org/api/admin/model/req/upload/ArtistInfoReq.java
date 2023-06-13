package org.api.admin.model.req.upload;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.core.model.convert.ArtistConvert;

@Data
@EqualsAndHashCode(callSuper = true)
public class ArtistInfoReq extends ArtistConvert {
}
