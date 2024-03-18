package org.api.admin.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AlbumListPageRes extends AlbumConvert {
    @Schema(name = "专辑歌曲数量")
    private Long albumSize;
    
    @Schema(name = "歌手信息")
    private List<ArtistConvert> artistList;
    
    @Schema(name = "排序", example = "sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序")
    private String orderBy;
    
    @Schema(name = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order;
}
