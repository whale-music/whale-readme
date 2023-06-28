package org.core.jpa.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("自定义查询 字典数据表")
public class SysDictDataEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 字典编码
     */
    @ApiModelProperty("字典编码")
    private Long id;
    
    
    /**
     * 字典排序
     */
    @ApiModelProperty("字典排序")
    private Integer dictSort;
    
    
    /**
     * 字典记录实际数据
     */
    @ApiModelProperty("字典记录实际数据")
    private String dictLabel;
    
    
    /**
     * 字典键值
     */
    @ApiModelProperty("字典键值")
    private String dictValue;
    
    
    /**
     * 字典类型
     */
    @ApiModelProperty("字典类型")
    private String dictType;
    
    
    /**
     * 状态（0正常 1停用）
     */
    @ApiModelProperty("状态（0正常 1停用）")
    private String status;
    
    
    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private String createBy;
    
    
    /**
     * 更新者
     */
    @ApiModelProperty("更新者")
    private String updateBy;
    
    
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    
    
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;
    
}
