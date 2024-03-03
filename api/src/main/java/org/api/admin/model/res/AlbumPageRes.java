package org.api.admin.model.res;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.ArtistConvert;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlbumPageRes {
    @Schema(title = "专辑表ID")
    private Long id;
    
    @ApiModelProperty("封面地址")
    private String picUrl;
    
    @Schema(title = "专辑名")
    private String albumName;
    
    @Schema(title = "专辑发布时间")
    private LocalDateTime publishTime;
    
    @Schema(title = "上传用户ID")
    private Long userId;
    
    @Schema(title = "修改时间")
    private LocalDateTime updateTime;
    
    @Schema(title = "创建时间")
    private LocalDateTime createTime;
    
    @ApiModelProperty("专辑歌曲数量")
    private Long albumSize;
    
    @ApiModelProperty("歌手信息")
    private List<AlbumArtistConvert> artistList;
    
    @ApiModelProperty(value = "排序", example = "sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序")
    private String orderBy;
    
    @ApiModelProperty(value = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlbumArtistConvert {
        @Schema(title = "歌手ID")
        private Long id;
        
        @Schema(title = "歌手名")
        @TableField("artist_name")
        private String artistName;
        
        @Schema(title = "歌手别名")
        @TableField("alias_name")
        private String aliasName;
        
        @ApiModelProperty("封面地址")
        private String picUrl;
        
        public AlbumArtistConvert(ArtistConvert artistConvert) {
            this(artistConvert.getId(), artistConvert.getArtistName(), artistConvert.getAliasName(), artistConvert.getPicUrl());
        }
    }
}
