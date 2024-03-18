package org.api.admin.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.TbResourcePojo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SaveOrUpdateMusicReq extends MusicConvert {
    
    @Schema(name = "音乐tag")
    private Set<String> musicTag = new HashSet<>();
    
    @Schema(name = "音乐流派")
    private Set<String> musicGenre = new HashSet<>();
    
    @Schema(name = "歌手名ID")
    private List<Long> artistIds;
    
    @Schema(name = "音源信息")
    private TbResourcePojo resource;
    
    @Schema(name = "音源临时地址")
    private String tempMusicFile;
    
    @Schema(name = "封面地址")
    private String tempPicFile;
}
