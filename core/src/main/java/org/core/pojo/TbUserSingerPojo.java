package org.core.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
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
 * @since 2022-10-29
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_user_singer")
@ApiModel(value = "TbUserSingerPojo对象", description = "用户关注歌曲家")
public class TbUserSingerPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;
    
    @TableField(value = "singer_id")
    private Long singerId;
    
    
}
