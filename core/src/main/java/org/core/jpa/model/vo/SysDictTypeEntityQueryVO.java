package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "自定义查询 字典类型表")
public class SysDictTypeEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 字典主键
     */
    @Schema(name = "字典主键")
    private Long id;
    
    
    /**
     * 字典名称
     */
    @Schema(name = "字典名称")
    private String dictName;
    
    
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
     * 备注
     */
    @Schema(name = "备注")
    private String remark;
    
    
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
     * 更新时间
     */
    @Schema(name = "更新时间")
    private LocalDateTime updateTime;
    
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
}
