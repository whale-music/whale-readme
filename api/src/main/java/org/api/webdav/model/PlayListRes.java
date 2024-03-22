package org.api.webdav.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbMusicPojo;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlayListRes extends TbMusicPojo {
    private Integer rate;
    
    public PlayListRes(TbMusicPojo pojo) {
        super(pojo.getId(),
                pojo.getMusicName(),
                pojo.getAliasName(),
                pojo.getAlbumId(),
                pojo.getUserId(),
                pojo.getTimeLength(),
                pojo.getComment(),
                pojo.getPublishTime(),
                pojo.getUpdateTime(),
                pojo.getCreateTime());
    }
    private String md5;
    private String path;
    private Long size;
}
