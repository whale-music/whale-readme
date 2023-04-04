package org.plugin.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.pojo.TbPluginMsgPojo;
import org.core.pojo.TbPluginPojo;
import org.core.pojo.TbPluginTaskPojo;
import org.core.service.TbPluginMsgService;
import org.core.service.TbPluginService;
import org.core.service.TbPluginTaskService;
import org.core.utils.UserUtil;
import org.mozilla.javascript.*;
import org.plugin.model.res.*;
import org.plugin.service.PluginService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PluginServiceImpl implements PluginService {
    
    @Autowired
    private TbPluginService pluginService;
    
    @Autowired
    private TbPluginMsgService pluginMsgService;
    
    @Autowired
    private TbPluginTaskService pluginTaskService;
    
    @Autowired
    private PluginPackage pluginPackage;
    
    @Override
    public List<PluginRes> getAllPlugin(Long userId, Long pluginId) {
        LambdaQueryWrapper<TbPluginPojo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(pluginId != null, TbPluginPojo::getId, pluginId);
        wrapper.eq(TbPluginPojo::getUserId, userId);
        List<TbPluginPojo> list = pluginService.list(wrapper);
        ArrayList<PluginRes> pluginRes = new ArrayList<>();
        for (TbPluginPojo tbPluginPojo : list) {
            PluginRes p = new PluginRes();
            BeanUtils.copyProperties(tbPluginPojo, p);
            pluginRes.add(p);
        }
        return pluginRes;
    }
    
    /**
     * 添加插件代码
     *
     * @param req 更新或添加插件代码
     */
    @Override
    public PluginRes saveOrUpdatePlugin(PluginReq req) {
        req.setUserId(req.getUserId() == null ? UserUtil.getUser().getId() : req.getUserId());
        pluginService.saveOrUpdate(req);
        TbPluginPojo byId = pluginService.getById(req.getId());
        PluginRes pluginRes = new PluginRes();
        BeanUtils.copyProperties(byId, pluginRes);
        return pluginRes;
    }
    
    /**
     * 查询插件入参
     *
     * @param pluginId 插件ID
     * @return 插件入参
     */
    @Override
    public List<PluginLabelValue> getPluginParams(Long pluginId) {
        TbPluginPojo byId = pluginService.getById(pluginId);
        if (byId == null) {
            return Collections.emptyList();
        }
        try (Context ctx = Context.enter()) {
            Scriptable scope = ctx.initStandardObjects();
            ctx.evaluateString(scope, byId.getCode(), null, 0, null);
            Function getParams = (Function) scope.get("getParams", scope);
            Object result = getParams.call(ctx, scope, scope, null);
            PluginLabelValueListRes pluginLabelValue = JSON.parseObject(JSON.toJSONString(result), PluginLabelValueListRes.class);
            return pluginLabelValue.getParams();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException(ResultCode.PLUGIN_CODE.getCode(), e.getMessage());
        }
    }
    
    /**
     * 运行插件任务
     *
     * @param req      插件入参
     * @param pluginId 插件ID
     * @param onLine   是否在线运行
     * @param taskId   任务ID
     */
    @Async
    @Override
    public void execPluginTask(List<PluginLabelValue> req, Long pluginId, Boolean onLine, Long taskId) {
        TbPluginPojo byId = pluginService.getById(pluginId);
        if (byId == null) {
            throw new BaseException(ResultCode.PLUGIN_EXISTED);
        }
        try (Context ctx = Context.enter()) {
            execCode(req, taskId, byId.getCode(), ctx);
            TbPluginTaskPojo entity = new TbPluginTaskPojo();
            entity.setId(taskId);
            entity.setStatus((short) 1);
            pluginTaskService.updateById(entity);
        } catch (Exception e) {
            TbPluginTaskPojo entity = new TbPluginTaskPojo();
            entity.setId(taskId);
            entity.setStatus((short) 2);
            pluginTaskService.updateById(entity);
            pluginPackage.log(taskId.toString(), String.valueOf(entity.getUserId()), e.getMessage());
            log.error(e.getMessage(), e);
            throw new BaseException(ResultCode.PLUGIN_CODE.getCode(), e.getMessage());
        }
    }
    
    private Object execCode(List<PluginLabelValue> req, Long id, String code, Context ctx) {
        Scriptable scope = ctx.initStandardObjects();
        ctx.evaluateString(scope, code, "<cmd>", 0, null);
        Function getParams = (Function) scope.get("saveMusic", scope);
        Map<String, String> map = req.stream().collect(Collectors.toMap(PluginLabelValue::getKey, PluginLabelValue::getValue));
        return getParams.call(ctx, scope, scope, List.of(JSON.toJSONString(map), id, pluginPackage).toArray());
    }
    
    /**
     * @param runtimeId 任务ID
     */
    @Override
    public List<PluginMsgRes> getPluginRuntimeMessages(Long runtimeId) {
        List<TbPluginMsgPojo> list = pluginMsgService.list(Wrappers.<TbPluginMsgPojo>lambdaQuery().eq(TbPluginMsgPojo::getTaskId, runtimeId));
        ArrayList<PluginMsgRes> pluginMsgRes = new ArrayList<>();
        for (TbPluginMsgPojo tbPluginMsgPojo : list) {
            PluginMsgRes pluginRes = new PluginMsgRes();
            BeanUtils.copyProperties(tbPluginMsgPojo, pluginRes);
            pluginMsgRes.add(pluginRes);
        }
        return pluginMsgRes;
    }
    
    /**
     * @param req      插件入参
     * @param pluginId 插件ID
     * @param taskId   任务ID
     */
    @Override
    public List<TbPluginMsgPojo> onLineExecPluginTask(List<PluginLabelValue> req, Long pluginId, Long taskId) {
        TbPluginPojo byId = pluginService.getById(pluginId);
        if (byId == null) {
            throw new BaseException(ResultCode.PLUGIN_EXISTED);
        }
        try (Context ctx = Context.enter()) {
            Object call = execCode(req, taskId, byId.getCode(), ctx);
            
            List<TbPluginMsgPojo> tbPluginMsgPojos = new ArrayList<>();
            if (call instanceof NativeArray array) {
                for (Object o : array) {
                    TbPluginMsgPojo e = new TbPluginMsgPojo();
                    NativeObject object = (NativeObject) o;
                    e.setPluginId(pluginId);
                    e.setTaskId(taskId);
                    long date = Long.parseLong(object.get("date").toString());
                    LocalDateTime of = LocalDateTimeUtil.of(date);
                    e.setCreateTime(of);
                    e.setUpdateTime(of);
                    e.setMsg(JSON.toJSONString(object.get("params")));
                    tbPluginMsgPojos.add(e);
                }
                return tbPluginMsgPojos;
            }
            return tbPluginMsgPojos;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException(ResultCode.PLUGIN_CODE.getCode(), e.getMessage());
        }
    }
    
    
    @Override
    public TbPluginTaskPojo getTbPluginTaskPojo(Long pluginId) {
        TbPluginTaskPojo entity = new TbPluginTaskPojo();
        entity.setPluginId(pluginId);
        entity.setUserId(UserUtil.getUser().getId());
        entity.setStatus((short) 0);
        pluginTaskService.save(entity);
        return entity;
    }
    
    /**
     * @param taskPojo 任务运行信息
     * @return 返回运行信息
     */
    @Override
    public List<TbPluginTaskPojo> getPluginRuntimeTask(TbPluginTaskPojo taskPojo) {
        if (taskPojo.getId() != null) {
            TbPluginTaskPojo byId = pluginTaskService.getById(taskPojo.getId());
            return Collections.singletonList(byId);
        }
        if (taskPojo.getUserId() != null) {
            LambdaQueryWrapper<TbPluginTaskPojo> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(TbPluginTaskPojo::getUserId, taskPojo.getUserId());
            wrapper.eq(taskPojo.getStatus() != null, TbPluginTaskPojo::getStatus, taskPojo.getStatus());
            wrapper.eq(taskPojo.getPluginId() != null, TbPluginTaskPojo::getPluginId, taskPojo.getPluginId());
            return pluginTaskService.list(wrapper);
        }
        return Collections.emptyList();
    }
}
