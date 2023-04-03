package org.plugin.service.impl;

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
            throw new BaseException(ResultCode.PLUGIN_CODE);
        }
    }
    
    /**
     * 运行插件代码
     *
     * @param pluginId 插件ID
     * @param id       任务ID
     */
    @Async
    @Override
    public void execPluginTask(Long pluginId, Boolean onLine, Long id) {
        List<PluginLabelValue> req = getPluginParams(pluginId);
        TbPluginPojo byId = pluginService.getById(pluginId);
        if (byId == null) {
            throw new BaseException(ResultCode.PLUGIN_EXISTED);
        }
        try (Context ctx = Context.enter()) {
            Scriptable scope = ctx.initStandardObjects();
            ctx.evaluateString(scope, byId.getCode(), "<cmd>", 0, null);
            Function getParams = (Function) scope.get("saveMusic", scope);
            Map<String, String> map = req.stream().collect(Collectors.toMap(PluginLabelValue::getKey, PluginLabelValue::getValue));
            getParams.call(ctx, scope, scope, List.of(map, id, pluginPackage).toArray());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // Context.exit();
            throw new BaseException(ResultCode.PLUGIN_CODE.getCode(), e.getMessage());
        }
    }
    
    /**
     * @param runtimeId
     * @return
     */
    @Override
    public List<PluginMsgRes> getPluginRuntimeMessages(Long runtimeId) {
        List<TbPluginMsgPojo> list = pluginMsgService.list(Wrappers.<TbPluginMsgPojo>lambdaQuery().eq(TbPluginMsgPojo::getTaskId, runtimeId));
        ArrayList<PluginMsgRes> pluginMsgRes = new ArrayList<>();
        BeanUtils.copyProperties(list, pluginMsgRes);
        return pluginMsgRes;
    }
    
    /**
     * @param pluginId
     * @param id
     * @return
     */
    @Override
    public List<TbPluginMsgPojo> onLineExecPluginTask(Long pluginId, Long id) {
        TbPluginPojo byId = pluginService.getById(pluginId);
        if (byId == null) {
            throw new BaseException(ResultCode.PLUGIN_EXISTED);
        }
        List<PluginLabelValue> req = getPluginParams(pluginId);
        TbPluginTaskPojo taskPojo = getTbPluginTaskPojo(pluginId);
        try (Context ctx = Context.enter()) {
            Scriptable scope = ctx.initStandardObjects();
            ctx.evaluateString(scope, byId.getCode(), "<cmd>", 0, null);
            Function getParams = (Function) scope.get("saveMusic", scope);
            Map<String, String> map = req.stream().collect(Collectors.toMap(PluginLabelValue::getKey, PluginLabelValue::getValue));
            Object call = getParams.call(ctx, scope, scope, List.of(map, taskPojo.getId(), pluginPackage).toArray());
            List<TbPluginMsgPojo> tbPluginMsgPojos = new ArrayList<>();
            if (call instanceof NativeArray) {
                ((NativeArray) call).forEach(o -> {
                    TbPluginMsgPojo e = new TbPluginMsgPojo();
                    NativeObject object = (NativeObject) o;
                    e.setPluginId(pluginId);
                    e.setTaskId(id);
                    Long date = Long.valueOf(object.get("date").toString());
                    e.setCreateTime(date);
                    e.setUpdateTime(date);
                    e.setMsg(String.valueOf(object.get("params")));
                    tbPluginMsgPojos.add(e);
                });
                return tbPluginMsgPojos;
            }
            return tbPluginMsgPojos;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // Context.exit();
            throw new BaseException(ResultCode.PLUGIN_CODE.getCode(), e.getMessage());
        }
    }
    
    
    @Override
    public TbPluginTaskPojo getTbPluginTaskPojo(Long pluginId) {
        TbPluginTaskPojo entity = new TbPluginTaskPojo();
        entity.setPluginId(pluginId);
        entity.setUserId(UserUtil.getUser().getId());
        pluginTaskService.save(entity);
        return entity;
    }
}
