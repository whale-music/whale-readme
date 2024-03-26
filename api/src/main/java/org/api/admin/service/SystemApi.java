package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.PageReqCommon;
import org.api.admin.model.common.PageResCommon;
import org.api.admin.model.req.SystemLogRes;
import org.api.admin.model.res.SystemLogReq;
import org.core.mybatis.iservice.SysLogService;
import org.core.mybatis.pojo.SysLogPojo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service(AdminConfig.ADMIN + "SystemApi")
public class SystemApi {
    
    private final SysLogService sysLogService;
    
    public SystemApi(SysLogService sysLogService) {
        this.sysLogService = sysLogService;
    }
    
    public PageResCommon<SystemLogRes> getSystemLogPage(SystemLogReq req) {
        LambdaQueryWrapper<SysLogPojo> wrapper = Wrappers.lambdaQuery();
        
        boolean order = StringUtils.equals(req.getOrder(), "desc");
        switch (req.getOrderBy()) {
            case "executionTime" -> wrapper.orderBy(true, order, SysLogPojo::getExecutionTime);
            case "updateTime" -> wrapper.orderBy(true, order, SysLogPojo::getUpdateTime);
            // case "createTime" ->
            default -> wrapper.orderBy(true, order, SysLogPojo::getCreateTime);
        }
        
        wrapper.in(CollUtil.isNotEmpty(req.getLogName()), SysLogPojo::getLogName, req.getLogName());
        wrapper.in(CollUtil.isNotEmpty(req.getRequestMethod()), SysLogPojo::getRequestMethod, req.getRequestMethod());
        wrapper.eq(StringUtils.isNotBlank(req.getRemoteHost()), SysLogPojo::getRemoteHost, req.getRemoteHost());
        wrapper.eq(StringUtils.isNotBlank(req.getMappingPath()), SysLogPojo::getMappingPath, req.getMappingPath());
        wrapper.like(StringUtils.isNotBlank(req.getMethodName()), SysLogPojo::getMethodName, req.getMethodName());
        wrapper.in(CollUtil.isNotEmpty(req.getOs()), SysLogPojo::getPlatform, req.getOs());
        
        // 是否执行成功，如果为空则全查
        String successStr = req.getSuccess();
        if (StringUtils.isNotBlank(successStr) && !StringUtils.equals(successStr, "all")) {
            if (StringUtils.equals(successStr, "success")) {
                wrapper.isNull(SysLogPojo::getExceptionClassName);
            } else {
                wrapper.isNotNull(SysLogPojo::getExceptionClassName);
            }
        }
        List<LocalDateTime> betweenDate = req.getBetweenDate();
        if (CollUtil.isNotEmpty(betweenDate) && betweenDate.size() == 2) {
            wrapper.ge(SysLogPojo::getStartTime, req.getBetweenDate().get(0));
            wrapper.le(SysLogPojo::getStartTime, req.getBetweenDate().get(1));
        }
        
        PageReqCommon pageCommon = req.getPageCommon();
        Page<SysLogPojo> page = sysLogService.page(Page.of(pageCommon.getPageIndex(), pageCommon.getPageNum()), wrapper);
        ArrayList<SystemLogRes> content = new ArrayList<>();
        List<SysLogPojo> records = page.getRecords();
        
        for (SysLogPojo sysLogPojo : records) {
            // 创建 SystemLogRes 实例
            SystemLogRes systemLogRes = new SystemLogRes();
            systemLogRes.setId(systemLogRes.getId());
            // 设置日志名称
            systemLogRes.setLogName(sysLogPojo.getLogName());
            // 设置执行时间
            systemLogRes.setExecutionTime(sysLogPojo.getExecutionTime());
            // 设置开始时间
            systemLogRes.setStartTime(sysLogPojo.getStartTime());
            // 设置结束时间
            systemLogRes.setEndTime(sysLogPojo.getEndTime());
            // 设置线程ID
            systemLogRes.setThreadId(sysLogPojo.getThreadId());
            // 设置线程名
            systemLogRes.setThreadName(sysLogPojo.getThreadName());
            // 设置进程ID
            systemLogRes.setProcessId(sysLogPojo.getProcessId());
            // 设置请求方法
            systemLogRes.setRequestMethod(sysLogPojo.getRequestMethod());
            
            SystemLogRes.WebRequest webRequest = new SystemLogRes.WebRequest();
            // 设置接口路径参数
            webRequest.setFullPath(sysLogPojo.getFullPath());
            // 设置请求host
            webRequest.setRemoteHost(sysLogPojo.getRemoteHost());
            // 设置接口路径
            webRequest.setMappingPath(sysLogPojo.getMappingPath());
            systemLogRes.setWebRequest(webRequest);
            
            // 设置 方法调用详情
            SystemLogRes.MethodCallDetail methodCallDetail = new SystemLogRes.MethodCallDetail();
            // 设置请求参数数据
            methodCallDetail.setMethodParamsData(sysLogPojo.getMethodParamsData());
            // 设置入参列表
            methodCallDetail.setMethodParamsType(sysLogPojo.getMethodParamsType());
            // 设置返回参数列表
            methodCallDetail.setMethodResultParamType(sysLogPojo.getMethodResultParamType());
            // 设置全类方法名
            methodCallDetail.setMethodName(sysLogPojo.getMethodName());
            // 将方法调用详情设置到 SystemLogRes 中
            systemLogRes.setMethodCallDetail(methodCallDetail);
            
            // 设置 Devices，设备信息
            SystemLogRes.Devices devices = new SystemLogRes.Devices();
            // 设置 User-Agent
            devices.setUserAgentContent(sysLogPojo.getUserAgentContent());
            // 设置是否为移动平台
            devices.setMobile(sysLogPojo.getMobile());
            // 设置浏览器类型
            devices.setBrowser(sysLogPojo.getBrowser());
            // 设置浏览器版本
            devices.setBrowserVersion(sysLogPojo.getBrowserVersion());
            // 设置平台类型
            devices.setPlatform(sysLogPojo.getPlatform());
            // 设置系统类型
            devices.setOs(sysLogPojo.getOs());
            // 设置系统版本
            devices.setOsVersion(sysLogPojo.getOsVersion());
            // 设置渲染引擎类型
            devices.setRenderingEngine(sysLogPojo.getRenderingEngine());
            // 设置渲染引擎版本
            devices.setRenderingEngineVersion(sysLogPojo.getRenderingEngineVersion());
            // 将设备信息设置到 SystemLogRes 中
            systemLogRes.setDevices(devices);
            
            // 设置 ExceptionReport，异常信息
            if (StringUtils.isNotBlank(sysLogPojo.getExceptionMessage())
                    && StringUtils.isNotBlank(sysLogPojo.getExceptionStack())
                    && Objects.nonNull(sysLogPojo.getExceptionLineNumber())
                    && StringUtils.isNotBlank(sysLogPojo.getExceptionClassName())
            ) {
                SystemLogRes.ExceptionReport exceptionReport = new SystemLogRes.ExceptionReport();
                // 设置异常信息
                exceptionReport.setExceptionMessage(sysLogPojo.getExceptionMessage());
                // 设置异常栈
                exceptionReport.setExceptionStack(sysLogPojo.getExceptionStack());
                // 设置异常方法行号
                exceptionReport.setExceptionLineNumber(sysLogPojo.getExceptionLineNumber());
                // 设置抛出异常类
                exceptionReport.setExceptionClassName(sysLogPojo.getExceptionClassName());
                // 将异常信息设置到 SystemLogRes 中
                systemLogRes.setExceptionReport(exceptionReport);
            }
            
            content.add(systemLogRes);
        }
        PageResCommon<SystemLogRes> res = new PageResCommon<>();
        res.setTotal(page.getTotal());
        res.setCurrent(page.getCurrent());
        res.setSize(page.getSize());
        res.setContent(content);
        return res;
    }
    
    public void removeSystemLog(Integer day) {
        LambdaQueryWrapper<SysLogPojo> wrapper = Wrappers.lambdaQuery();
        if (Objects.nonNull(day)) {
            DateTime dateTime = DateUtil.offsetDay(new Date(), -day);
            wrapper.le(SysLogPojo::getCreateTime, dateTime.toLocalDateTime());
        }
        sysLogService.remove(wrapper);
    }
}
