package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(title = "自定义查询 插件消息表")
public class TbPluginMsgEntityQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 插件消息ID
     */
    @Schema(name = "插件消息ID")
    private Long id;
    
    
    /**
     * 插件ID
     */
    @Schema(name = "插件ID")
    private Long pluginId;
    
    
    /**
     * 任务ID
     */
    @Schema(name = "任务ID")
    private Long taskId;
    
    
    /**
     * 用户ID
     */
    @Schema(name = "用户ID")
    private Long userId;
    
    
    /**
     * 插件消息等级,0 info 1 debug 2 warn 3 error
     */
    @Schema(name = "插件消息等级,0 info 1 debug 2 warn 3 error")
    private Integer level;
    
    
    /**
     * 插件运行消息
     */
    @Schema(name = "插件运行消息")
    private String msg;
    
    
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
