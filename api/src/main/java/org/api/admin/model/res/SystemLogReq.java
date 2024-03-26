package org.api.admin.model.res;

import cn.hutool.core.collection.CollUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.model.common.PageReqCommon;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SystemLogReq extends PageReqCommon {
    @Schema(name = "Log日志名")
    private List<String> logName;
    
    @Schema(name = "web请求方法")
    private List<String> requestMethod;
    
    @Schema(name = "平台", example = "Windows, MacOS, Linux, Android, Iphone")
    private List<String> os;
    
    @Schema(name = "请求主机")
    private String remoteHost;
    
    @Schema(name = "映射路径")
    private String mappingPath;
    
    @Schema(name = "执行方法名")
    private String methodName;
    
    @Schema(name = "是否执行成功")
    private String success;
    
    @Schema(name = "执行时间(大于)")
    private String executionTime;
    
    @Schema(name = "结束时间")
    private List<LocalDateTime> betweenDate;
    
    @Schema(name = "排序字段")
    private String orderBy;
    
    @Schema(name = "排序")
    private String order = "desc";
    
    
    public List<String> getLogName() {
        if (CollUtil.isEmpty(this.logName)) {
            return Collections.emptyList();
        }
        return this.logName.parallelStream().map(StringUtils::trim).toList();
    }
    
    public List<String> getRequestMethod() {
        if (CollUtil.isEmpty(this.requestMethod)) {
            return Collections.emptyList();
        }
        return this.requestMethod.parallelStream().map(StringUtils::trim).toList();
    }
    
    public List<String> getOs() {
        if (CollUtil.isEmpty(this.os)) {
            return Collections.emptyList();
        }
        return this.os.parallelStream().map(StringUtils::trim).toList();
    }
    
    public String getRemoteHost() {
        return StringUtils.trim(remoteHost);
    }
    
    public String getMappingPath() {
        return StringUtils.trim(this.mappingPath);
    }
    
    public String getMethodName() {
        return StringUtils.trim(this.methodName);
    }
}
