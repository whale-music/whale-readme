package org.api.webdav.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.jpa.entity.TbMusicEntity;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlayListRes extends TbMusicEntity {
    private String md5;
    private String path;
    private Long size;
}
