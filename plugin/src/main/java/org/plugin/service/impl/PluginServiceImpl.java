package org.plugin.service.impl;

import com.alibaba.fastjson2.JSON;
import org.api.admin.service.MusicFlowApi;
import org.core.common.exception.BaseException;
import org.core.common.page.LambdaQueryWrapper;
import org.core.common.result.ResultCode;
import org.core.iservice.PluginService;
import org.core.pojo.PluginPojo;
import org.core.service.QukuService;
import org.core.utils.UserUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.plugin.model.res.PluginLabelValue;
import org.plugin.model.res.PluginLabelValueListRes;
import org.plugin.model.res.PluginReq;
import org.plugin.model.res.PluginRes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("PluginServiceHandle")
public class PluginServiceImpl implements org.plugin.service.PluginService {
    
    @Autowired
    private PluginService pluginService;
    
    @Autowired
    private QukuService qukuService;
    
    @Autowired
    private MusicFlowApi musicFlowApi;
    
    @Override
    public List<PluginRes> getAllPlugin(Long userId, Long pluginId) {
        LambdaQueryWrapper<PluginPojo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(pluginId != null, PluginPojo::getId, pluginId);
        wrapper.eq(PluginPojo::getUserId, userId);
        List<PluginPojo> list = pluginService.list(wrapper);
        ArrayList<PluginRes> pluginRes = new ArrayList<>();
        for (PluginPojo pluginPojo : list) {
            PluginRes p = new PluginRes();
            BeanUtils.copyProperties(pluginPojo, p);
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
        PluginPojo byId = pluginService.getById(req.getId());
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
        PluginPojo byId = pluginService.getById(pluginId);
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
            Context.exit();
            throw new BaseException(ResultCode.PLUGIN_CODE);
        }
    }
    
    @Async
    @Override
    public void execPluginTask(Long pluginId, List<PluginLabelValue> req) {
        PluginPojo byId = pluginService.getById(pluginId);
        if (byId == null) {
            throw new BaseException(ResultCode.PLUGIN_EXISTED);
        }
        try (Context ctx = Context.enter()) {
            Scriptable scope = ctx.initStandardObjects();
            ctx.evaluateString(scope, byId.getCode(), "<cmd>", 0, null);
            Function getParams = (Function) scope.get("saveMusic", scope);
            Map<String, String> map = req.stream().collect(Collectors.toMap(PluginLabelValue::getKey, PluginLabelValue::getValue));
            getParams.call(ctx, scope, scope, List.of(map, qukuService, musicFlowApi).toArray());
        } catch (Exception e) {
            Context.exit();
            throw new BaseException(ResultCode.PLUGIN_CODE);
        }
    }
}
