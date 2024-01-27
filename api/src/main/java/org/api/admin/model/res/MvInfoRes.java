package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.SimpleArtist;
import org.core.mybatis.pojo.TbMvInfoPojo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MvInfoRes extends TbMvInfoPojo {
    private String mvUrl;
    private String picUrl;
    private List<String> tags;
    private List<SimpleArtist> artists;
}
