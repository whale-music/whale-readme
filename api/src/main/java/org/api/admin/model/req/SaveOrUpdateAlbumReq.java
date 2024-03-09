package org.api.admin.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbAlbumPojo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SaveOrUpdateAlbumReq extends TbAlbumPojo {
    
    private List<String> albumGenre;
    private List<Long> artistIds;
    private String tempFile;
}
