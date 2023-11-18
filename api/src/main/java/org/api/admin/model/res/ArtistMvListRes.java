package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.SimpleArtist;
import org.core.mybatis.pojo.TbMvPojo;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ArtistMvListRes extends TbMvPojo {
    private String mvUrl;
    private String picUrl;
    private List<String> tags;
    private List<SimpleArtist> artists;
    
    public ArtistMvListRes(TbMvPojo tbMvPojo) {
        BeanUtils.copyProperties(tbMvPojo, this);
    }
}
