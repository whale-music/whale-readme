package org.api.admin.model.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.TbResourcePojo;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SaveOrUpdateMusicReq extends MusicConvert {
    @ApiModelProperty("歌手名ID")
    private List<Long> artistIds;
    
    @ApiModelProperty("音源信息")
    private TbResourcePojo resource;
    
    @ApiModelProperty("音源临时地址")
    private String tempMusicFile;
    
    @ApiModelProperty("封面地址")
    private String tempPicFile;
}
