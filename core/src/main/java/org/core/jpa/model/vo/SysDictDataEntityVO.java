package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Schema(title = "保存 字典数据表")
public class SysDictDataEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 字典编码
     */
    @NotNull(message = "id can not null")
    @Schema(name = "字典编码")
    private Long id;
    
    
    /**
     * 字典排序
     */
    @Schema(name = "字典排序")
    private Integer dictSort;
    
    
    /**
     * 字典记录实际数据
     */
    @Schema(name = "字典记录实际数据")
    private String dictLabel;
    
    
    /**
     * 字典键值
     */
    @Schema(name = "字典键值")
    private String dictValue;
    
    
    /**
     * 字典类型
     */
    @Schema(name = "字典类型")
    private String dictType;
    
    
    /**
     * 状态（0正常 1停用）
     */
    @Schema(name = "状态（0正常 1停用）")
    private String status;
    
    
    /**
     * 创建者
     */
    @Schema(name = "创建者")
    private String createBy;
    
    
    /**
     * 更新者
     */
    @Schema(name = "更新者")
    private String updateBy;
    
    
    /**
     * 备注
     */
    @Schema(name = "备注")
    private String remark;
    
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 更新时间
     */
    @Schema(name = "更新时间")
    private LocalDateTime updateTime;
    
}
