package org.core.mybatis.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author Sakura
 * @since 2023-06-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_origin")
@ApiModel(value = "TbOriginPojo对象", description = "音乐来源")
public class TbOriginPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @ApiModelProperty("音乐ID")
    @TableField("music_id")
    private Long musicId;
    
    @ApiModelProperty("源地址ID")
    @TableField("music_url_id")
    private Long musicUrlId;
    
    @ApiModelProperty("来源")
    @TableField("origin")
    private String origin;
    
    @ApiModelProperty("来源地址")
    @TableField("origin_url")
    private String originUrl;
    
    
}
