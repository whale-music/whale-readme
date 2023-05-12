package org.plugin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.compiler.CompilerUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.service.MusicFlowApi;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.PluginType;
import org.core.iservice.TbPluginMsgService;
import org.core.iservice.TbPluginService;
import org.core.iservice.TbPluginTaskService;
import org.core.pojo.TbPluginMsgPojo;
import org.core.pojo.TbPluginPojo;
import org.core.pojo.TbPluginTaskPojo;
import org.core.service.QukuService;
import org.core.utils.UserUtil;
import org.jetbrains.annotations.NotNull;
import org.plugin.common.ComboSearchPlugin;
import org.plugin.common.CommonPlugin;
import org.plugin.common.TaskStatus;
import org.plugin.converter.PluginLabelValue;
import org.plugin.converter.PluginMsgRes;
import org.plugin.converter.PluginReq;
import org.plugin.converter.PluginRes;
import org.plugin.model.PluginRunParamsRes;
import org.plugin.model.PluginTaskLogRes;
import org.plugin.service.PluginService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PluginServiceImpl implements PluginService {
    @Autowired
    private MusicFlowApi musicFlowApi;
    
    @Autowired
    private TbPluginMsgService pluginMsgService;
    
    @Autowired
    private TbPluginTaskService pluginTaskService;
    
    @Autowired
    private TbPluginService pluginService;
    
    @Autowired
    private QukuService qukuService;
    
    private static CommonPlugin runCommonCode(String script, String allClassName) {
        try {
            final ClassLoader classLoader = CompilerUtil.getCompiler(null)
                                                        // 被编译的源码字符串
                                                        .addSource(allClassName, script).compile();
            final Class<?> clazz = classLoader.loadClass(allClassName);
            log.info("clazz: {}", clazz);
            // 实例化对象c
            Object obj = ReflectUtil.newInstance(clazz);
            if (obj instanceof CommonPlugin) {
                return (CommonPlugin) obj;
            }
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage());
        }
        log.error("全类名: {}", allClassName);
        log.error("script: {}", script);
        throw new BaseException(ResultCode.PLUGIN_CODE);
    }
    
    private static ComboSearchPlugin runInteractiveCode(String script, String allClassName) {
        try {
            final ClassLoader classLoader = CompilerUtil.getCompiler(null)
                                                        // 被编译的源码字符串
                                                        .addSource(allClassName, script).compile();
            final Class<?> clazz = classLoader.loadClass(allClassName);
            log.info("clazz: {}", clazz);
            // 实例化对象c
            Object obj = ReflectUtil.newInstance(clazz);
            if (obj instanceof ComboSearchPlugin) {
                return (ComboSearchPlugin) obj;
            }
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new BaseException(e.getMessage());
        }
        log.error("全类名: {}", allClassName);
        log.error("script: {}", script);
        throw new BaseException(ResultCode.PLUGIN_CODE);
    }
    
    /**
     * 获取代码中全类名
     *
     * @param code 代码
     * @return 全类名
     */
    @NotNull
    private static String getClassName(String code) {
        List<String> packageList = ReUtil.findAll("package\\s[a-zA-Z]+[0-9a-zA-Z_]*(\\.[a-zA-Z]+[0-9a-zA-Z_]*)*\\.[a-zA-Z]+[0-9a-zA-Z_]*", code, 0);
        List<String> classList = ReUtil.findAll("[public]?class\\s(\\w+)\\b", code, 0);
        if (CollUtil.isEmpty(packageList) && packageList.size() == 1 && CollUtil.isEmpty(classList) && classList.size() == 1) {
            throw new NullPointerException();
        }
        String className = StringUtils.replace(classList.get(0), "public", "");
        className = StringUtils.replace(className, "class", "").trim();
        String packageStr = StringUtils.replace(packageList.get(0), "package ", "");
        return packageStr + "." + className;
    }
    
    @Override
    public List<PluginRes> getAllPlugin(Long userId, List<Long> pluginId, String name) {
        LambdaQueryWrapper<TbPluginPojo> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(CollUtil.isNotEmpty(pluginId), TbPluginPojo::getId, pluginId);
        wrapper.eq(TbPluginPojo::getUserId, userId);
        wrapper.like(org.apache.commons.lang3.StringUtils.isNotBlank(name), TbPluginPojo::getPluginName, name);
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
    public PluginRunParamsRes getPluginParams(Long pluginId) {
        TbPluginPojo byId = pluginService.getById(pluginId);
        if (byId == null) {
            throw new BaseException(ResultCode.PLUGIN_EXISTED);
        }
        String script = byId.getCode();
        if (org.apache.commons.lang3.StringUtils.isBlank(script)) {
            throw new BaseException(ResultCode.PLUGIN_CODE);
        }
        String allClassName = getClassName(script);
        switch (byId.getType()) {
            case PluginType.COMMON:
                CommonPlugin func = runCommonCode(script, allClassName);
                return new PluginRunParamsRes(func.getType(), func.getParams());
            case PluginType.INTERACTIVE:
                ComboSearchPlugin comboSearchPlugin = runInteractiveCode(script, allClassName);
                return new PluginRunParamsRes(comboSearchPlugin.getType(), comboSearchPlugin.getParams());
        }
        throw new BaseException(ResultCode.PLUGIN_NO_EXIST_EXISTED);
    }
    
    /**
     * 运行插件任务
     *
     * @param req      插件入参
     * @param pluginId 插件ID
     * @param onLine   是否在线运行
     * @param task     任务ID
     */
    @Async
    @Override
    public void execPluginTask(List<PluginLabelValue> req, Long pluginId, Boolean onLine, TbPluginTaskPojo task) {
        try {
            onLineExecPluginTask(req, pluginId, task);
            // 运行完成
            TbPluginTaskPojo entity = new TbPluginTaskPojo();
            entity.setId(task.getId());
            entity.setStatus(TaskStatus.STOP_STATUS);
            pluginTaskService.updateById(entity);
        } catch (Exception e) {
            // 运行错误
            TbPluginTaskPojo entity = new TbPluginTaskPojo();
            entity.setId(task.getId());
            entity.setStatus(TaskStatus.ERROR_STATUS);
            pluginTaskService.updateById(entity);
            PluginPackage pluginPackage = new PluginPackage(musicFlowApi,
                    pluginMsgService,
                    pluginTaskService,
                    qukuService,
                    task.getId(),
                    task.getUserId(),
                    null);
            pluginPackage.log((short) 3, "error错误: {}: {}", e.getClass().getName(), e.getMessage());
            log.error(e.getMessage(), e);
            throw new BaseException(ResultCode.PLUGIN_CODE.getCode(), e.getMessage());
        }
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
     * @param task     任务ID
     */
    @Override
    public List<TbPluginMsgPojo> onLineExecPluginTask(List<PluginLabelValue> req, Long pluginId, TbPluginTaskPojo task) {
        TbPluginPojo byId = pluginService.getById(pluginId);
        if (byId == null) {
            throw new BaseException(ResultCode.PLUGIN_EXISTED);
        }
        CommonPlugin func = runCommonCode(byId.getCode(), getClassName(byId.getCode()));
        PluginPackage pluginPackage = new PluginPackage(musicFlowApi,
                pluginMsgService,
                pluginTaskService,
                qukuService,
                task.getId(),
                task.getUserId(),
                null);
        func.apply(req, pluginPackage);
        TbPluginTaskPojo entity = new TbPluginTaskPojo();
        entity.setId(task.getId());
        entity.setStatus(TaskStatus.STOP_STATUS);
        pluginTaskService.updateById(entity);
        return pluginPackage.getLogs();
    }
    
    
    @Override
    public TbPluginTaskPojo getTbPluginTaskPojo(Long pluginId, List<PluginLabelValue> pluginLabelValue, Long userId) {
        TbPluginTaskPojo entity = new TbPluginTaskPojo();
        entity.setPluginId(pluginId);
        entity.setUserId(userId);
        entity.setParams(JSON.toJSONString(pluginLabelValue));
        entity.setStatus(TaskStatus.RUN_STATUS);
        pluginTaskService.save(entity);
        return entity;
    }
    
    /**
     * @param id       用户ID
     * @param type     插件类型
     * @param taskPojo 任务运行信息
     * @return 返回运行信息
     */
    @Override
    public List<TbPluginTaskPojo> getTask(Long id, String type, TbPluginTaskPojo taskPojo) {
        Collection<Long> pluginIds = null;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(type)) {
            List<TbPluginPojo> list = pluginService.list(Wrappers.<TbPluginPojo>lambdaQuery().in(TbPluginPojo::getType, type));
            pluginIds = list.parallelStream().map(TbPluginPojo::getId).collect(Collectors.toSet());
        }
        taskPojo = taskPojo == null ? new TbPluginTaskPojo() : taskPojo;
        LambdaQueryWrapper<TbPluginTaskPojo> wrappers = Wrappers.lambdaQuery();
        wrappers.eq(taskPojo.getId() != null, TbPluginTaskPojo::getId, taskPojo.getId());
        wrappers.eq(taskPojo.getStatus() != null, TbPluginTaskPojo::getStatus, taskPojo.getStatus());
        wrappers.eq(taskPojo.getPluginId() != null, TbPluginTaskPojo::getPluginId, taskPojo.getPluginId());
        wrappers.eq(taskPojo.getUserId() != null, TbPluginTaskPojo::getUserId, taskPojo.getUserId());
        wrappers.in(CollUtil.isNotEmpty(pluginIds), TbPluginTaskPojo::getPluginId, pluginIds);
        wrappers.orderByDesc(TbPluginTaskPojo::getCreateTime);
        return pluginTaskService.list(wrappers);
    }
    
    
    /**
     * 删除插件任务
     * 注意： 目前只能删除已完成或错误停止的任务
     *
     * @param id 任务ID
     */
    @Override
    public void deleteTask(List<Long> id) {
        if (CollUtil.isNotEmpty(id) && id.size() > 1) {
            List<TbPluginTaskPojo> tbPluginTaskPojos = pluginTaskService.listByIds(id);
            for (TbPluginTaskPojo tbPluginTaskPojo : tbPluginTaskPojos) {
                if (tbPluginTaskPojo.getStatus() == TaskStatus.RUN_STATUS) {
                    throw new BaseException(ResultCode.PLUGIN_CANNOT_DELETE_RUNNING);
                }
            }
        }
        pluginTaskService.removeBatchByIds(id);
        LambdaQueryWrapper<TbPluginMsgPojo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(TbPluginMsgPojo::getTaskId, id);
        pluginMsgService.remove(queryWrapper);
    }
    
    /**
     * 删除插件
     *
     * @param id 插件ID
     */
    @Override
    public void deletePlugin(Long id) {
        boolean b = pluginService.removeById(id);
        if (!b) {
            throw new BaseException(ResultCode.PLUGIN_DELETE_ERROR);
        }
    }
    
    /**
     * 聚合插件搜索
     *
     * @param pluginLabelValue 程序启动插件
     * @param pluginId         插件ID
     * @param name             搜索参数
     * @return 搜索返回数据
     */
    @Override
    public List<PluginLabelValue> getInteractiveSearch(List<PluginLabelValue> pluginLabelValue, Long pluginId, String name) {
        TbPluginPojo byId = pluginService.getById(pluginId);
        String code = byId.getCode();
        ComboSearchPlugin comboSearchPlugin = runInteractiveCode(code, getClassName(byId.getCode()));
        return comboSearchPlugin.search(pluginLabelValue, name);
    }
    
    /**
     * 运行聚合插件
     *
     * @param pluginLabelValue 插件启动入参
     * @param pluginId         插件ID
     * @param type             传入ID类型 Music ID Album ID Artist ID
     * @param id               ID
     * @param pojo             任务ID
     * @return 运行完成同步返回信息
     */
    @Override
    public PluginTaskLogRes execInteractivePluginTask(List<PluginLabelValue> pluginLabelValue, Long pluginId, String type, Long id, TbPluginTaskPojo pojo) {
        try {
            TbPluginPojo byId = pluginService.getById(pluginId);
            String code = byId.getCode();
            ComboSearchPlugin comboSearchPlugin = runInteractiveCode(code, getClassName(byId.getCode()));
            PluginPackage pluginPackage = new PluginPackage(musicFlowApi,
                    pluginMsgService,
                    pluginTaskService,
                    qukuService,
                    pojo.getId(),
                    pojo.getUserId(),
                    null);
            String sync = comboSearchPlugin.sync(pluginLabelValue, type, id, pluginPackage);
            List<TbPluginMsgPojo> logs = pluginPackage.getLogs();
            PluginTaskLogRes pluginTaskLogRes = new PluginTaskLogRes();
            pluginTaskLogRes.setPluginMsg(logs);
            pluginTaskLogRes.setHtml(sync);
            return pluginTaskLogRes;
        } catch (Exception e) {
            PluginTaskLogRes pluginTaskLogRes = new PluginTaskLogRes();
            pluginTaskLogRes.setHtml(e.getMessage());
            return pluginTaskLogRes;
        }
    }
}