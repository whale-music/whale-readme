package org.core.pojo;

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
 * 用户关注歌曲家
 * </p>
 *
 * @author Sakura
 * @since 2022-12-07
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_user_singer")
@ApiModel(value = "TbUserSingerPojo对象", description = "用户关注歌曲家")
public class TbUserSingerPojo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty("用户ID")
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;
    
    @ApiModelProperty("歌手ID")
    @TableField("singer_id")
    private Long singerId;


}
