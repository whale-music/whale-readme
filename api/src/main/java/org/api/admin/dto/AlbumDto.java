package org.api.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.TbAlbumPojo;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AlbumDto extends TbAlbumPojo {
}
