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
 * 封面中间表
 * </p>
 *
 * @author Sakura
 * @since 2023-06-13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("tb_middle_pic")
@ApiModel(value = "TbMiddlePicPojo对象", description = "封面中间表")
public class TbMiddlePicPojo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @ApiModelProperty("中间表")
    @TableField("middle_id")
    private Long middleId;
    
    @ApiModelProperty("封面ID")
    @TableField("pic_id")
    private Long picId;
    
    @ApiModelProperty("封面类型")
    @TableField("type")
    private Short type;
    
    
}
