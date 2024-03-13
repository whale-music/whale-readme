package org.core.jpa.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "字典数据表")
public class SysDictDataEntityDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 字典编码
     */
    @Schema(title = "字典编码")
    private Long id;
    
    
    /**
     * 字典排序
     */
    @Schema(title = "字典排序")
    private Integer dictSort;
    
    
    /**
     * 字典记录实际数据
     */
    @Schema(title = "字典记录实际数据")
    private String dictLabel;
    
    
    /**
     * 字典键值
     */
    @Schema(title = "字典键值")
    private String dictValue;
    
    
    /**
     * 字典类型
     */
    @Schema(title = "字典类型")
    private String dictType;
    
    
    /**
     * 状态（0正常 1停用）
     */
    @Schema(title = "状态（0正常 1停用）")
    private String status;
    
    
    /**
     * 创建者
     */
    @Schema(title = "创建者")
    private String createBy;
    
    
    /**
     * 更新者
     */
    @Schema(title = "更新者")
    private String updateBy;
    
    
    /**
     * 备注
     */
    @Schema(title = "备注")
    private String remark;
    
    
    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 更新时间
     */
    @Schema(title = "更新时间")
    private LocalDateTime updateTime;
    
}
