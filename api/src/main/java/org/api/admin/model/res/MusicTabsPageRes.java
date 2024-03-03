package org.api.admin.model.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class MusicTabsPageRes implements Serializable {
    
    @ApiModelProperty("音乐ID")
    private Long id;
    
    @ApiModelProperty("音乐名")
    private String musicName;
    
    @ApiModelProperty("音乐别名")
    private String musicNameAlias;
    
    @ApiModelProperty("封面")
    private String pic;
    
    @ApiModelProperty("歌手名ID")
    private List<Long> artistIds;
    
    @ApiModelProperty("歌手名")
    private List<String> artistNames;
    
    @ApiModelProperty("专辑")
    private Long albumId;
    
    @ApiModelProperty("专辑")
    private String albumName;
    
    @ApiModelProperty(value = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order;
    
    @ApiModelProperty("音乐是否存在")
    private Boolean isExist;
    
    @ApiModelProperty("音乐是否喜欢")
    private Boolean isLike;
    
    @ApiModelProperty("音乐地址")
    private String musicRawUrl;
    
    @ApiModelProperty("歌曲时长")
    private Integer timeLength;
    
    @ApiModelProperty("用户ID")
    private Long userId;
    
    @ApiModelProperty("用户昵称")
    private String nickname;
    
    @ApiModelProperty("发行时间")
    private LocalDateTime publishTime;
    
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
