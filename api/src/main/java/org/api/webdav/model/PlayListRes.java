package org.api.webdav.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbMusicPojo;
import org.core.mybatis.pojo.TbResourcePojo;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlayListRes extends TbMusicPojo {
    private String md5;
    private String path;
    private Long size;
    private List<TbResourcePojo> resourceList;
}
