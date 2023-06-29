package org.core.mybatis.pojo;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典类型表(SysDictType)表实体类
 *
 * @author Sakura
 * @since 2023-06-28 13:02:30
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_dict_type")
@Schema(name = "SysDictType", description = "字典类型表")
public class SysDictTypePojo extends Model<SysDictTypePojo> implements Serializable {
    public static final long serialVersionUID = -65261769780792020L;
    
    @Schema(title = "字典主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    
    @Schema(title = "字典名称")
    @TableField("dict_name")
    private String dictName;
    
    @Schema(title = "字典类型")
    @TableField("dict_type")
    private String dictType;
    
    @Schema(title = "状态（0正常 1停用）")
    @TableField("status")
    private String status;
    
    @Schema(title = "备注")
    @TableField("remark")
    private String remark;
    
    @Schema(title = "创建者")
    @TableField("create_by")
    private String createBy;
    
    @Schema(title = "更新者")
    @TableField("update_by")
    private String updateBy;
    
    @Schema(title = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @Schema(title = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    
}

