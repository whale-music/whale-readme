package org.api.admin.model.res;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class MusicPageRes implements Serializable {
    
    @ApiModelProperty("音乐ID")
    private Long id;
    
    @ApiModelProperty("音乐名")
    private String musicName;
    
    @ApiModelProperty("音乐别名")
    private String musicNameAlias;
    
    @ApiModelProperty("歌手")
    private List<Long> singerIds;
    
    @ApiModelProperty("歌手")
    private List<String> singerName;
    
    @ApiModelProperty("专辑")
    private Long albumId;
    
    @ApiModelProperty("专辑")
    private String albumName;
    
    @ApiModelProperty(value = "数据排序", example = "true: ASC, false: DESC")
    private Boolean order;
    
    @ApiModelProperty("音乐是否存在")
    private Boolean isExist;
    
    @ApiModelProperty("音乐地址")
    private String musicRawUrl;
    
    @ApiModelProperty("歌曲时长")
    @TableField("time_length")
    private Integer timeLength;
    
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
