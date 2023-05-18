package org.plugin.controller;

import org.core.common.result.R;
import org.core.pojo.TbPluginMsgPojo;
import org.core.pojo.TbPluginTaskPojo;
import org.core.pojo.TbScheduleTaskPojo;
import org.core.utils.UserUtil;
import org.plugin.common.TaskStatus;
import org.plugin.converter.*;
import org.plugin.model.PluginRunParamsRes;
import org.plugin.model.PluginTaskLogRes;
import org.plugin.service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController("plugins")
@RequestMapping("/admin")
public class PluginController {
    
    @Autowired
    private PluginService pluginService;
    
    @PostMapping("/saveOrUpdatePlugin")
    public R saveOrUpdatePlugin(@RequestBody PluginReq req) {
        PluginRes res = pluginService.saveOrUpdatePlugin(req);
        return R.success(res);
    }
    
    /**
     * 获取所有插件
     *
     * @param userId 用户ID
     * @return 插件信息
     */
    @GetMapping("/getAllPlugins")
    public R getAllPlugin(@RequestParam(value = "userId", required = false) Long userId, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "id", required = false) List<Long> id) {
        List<PluginRes> list = pluginService.getAllPlugin(userId == null ? UserUtil.getUser().getId() : userId, id, name);
        return R.success(list);
    }
    
    /**
     * 查询插件入参
     *
     * @param pluginId 插件ID
     * @return 插件入参
     */
    @GetMapping("/getPluginParams")
    public R getPluginParams(@RequestParam("pluginId") Long pluginId) {
        PluginRunParamsRes pluginParams = pluginService.getPluginParams(pluginId);
        return R.success(pluginParams);
    }
    
    
    @PostMapping("/interactive/search")
    public R getInteractiveSearch(@RequestBody List<PluginLabelValue> pluginLabelValue, @RequestParam("pluginId") Long pluginId, @RequestParam("name") String name) {
        List<PluginLabelValue> list = pluginService.getInteractiveSearch(pluginLabelValue, pluginId, name);
        return R.success(list);
    }
    
    /**
     * 运行普通插件任务
     *
     * @param pluginId 插件ID
     */
    @PostMapping("/execPluginTask/common")
    public R execCommonPluginTask(@RequestParam("pluginId") Long pluginId, @RequestBody List<PluginLabelValue> pluginLabelValue, @RequestParam(value = "onLine", required = false, defaultValue = "true") Boolean onLine) {
        TbPluginTaskPojo pojo = pluginService.getTbPluginTaskPojo(pluginId, pluginLabelValue, UserUtil.getUser().getId());
        if (Boolean.TRUE.equals(onLine)) {
            List<TbPluginMsgPojo> tbPluginMsgPojos = pluginService.onLineExecPluginTask(pluginLabelValue, pluginId, pojo);
            return R.success(tbPluginMsgPojos);
        } else {
            pluginService.execPluginTask(pluginLabelValue, pluginId, onLine, pojo);
            return R.success(pojo.getId());
        }
    }
    
    /**
     * 运行聚合插件
     *
     * @param pluginId 插件ID
     */
    @PostMapping("/execPluginTask/interactive")
    public R execInteractivePluginTask(@RequestParam("pluginId") Long pluginId, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "id", required = false) Long id, @RequestBody List<PluginLabelValue> pluginLabelValue) {
        TbPluginTaskPojo pojo = pluginService.getTbPluginTaskPojo(pluginId, pluginLabelValue, UserUtil.getUser().getId());
        pojo.setStatus(TaskStatus.STOP_STATUS);
        PluginTaskLogRes res = pluginService.execInteractivePluginTask(pluginLabelValue, pluginId, type, id, pojo);
        return R.success(res);
    }
    
    @PostMapping("/getTask")
    public R getTask(@RequestParam(value = "type", required = false) String type, @RequestBody TbPluginTaskPojo taskPojo) {
        List<TbPluginTaskPojo> list = pluginService.getTask(UserUtil.getUser().getId(), type, taskPojo);
        return R.success(list);
    }
    
    @GetMapping("/getPluginRuntimeMessages")
    public R getPluginRuntimeMessages(@RequestParam("runtimeId") Long runtimeId) {
        List<PluginMsgRes> list = pluginService.getPluginRuntimeMessages(runtimeId);
        return R.success(list);
    }
    
    @GetMapping("/deleteTask")
    public R deleteTask(@RequestParam("id") List<Long> id) {
        pluginService.deleteTask(id);
        return R.success();
    }
    
    @GetMapping("/deletePlugin/{id}")
    public R deletePlugin(@PathVariable("id") Long id) {
        pluginService.deletePlugin(id);
        return R.success();
    }
    
    
    /**
     * 查看动态任务
     */
    @PostMapping("/schedule/get/task")
    public R getStartingDynamicTask(@RequestBody(required = false) TbScheduleTaskPojo req, @RequestParam(value = "id", required = false) Long id) {
        TbScheduleTaskPojo pojo = Optional.ofNullable(req).orElse(new TbScheduleTaskPojo());
        Long userId = id == null ? UserUtil.getUser().getId() : id;
        List<ScheduleRes> list = pluginService.getSchedulerTaskList(userId, pojo);
        return R.success(list);
    }
    
    
    /**
     * 保存动态任务
     *
     * @param task 任务信息
     */
    @PostMapping("/schedule/task")
    public R saveOrUpdateDynamicTask(@RequestBody TbScheduleTaskPojo task, @RequestParam("isRun") Boolean isRun) {
        pluginService.saveOrUpdateDynamicTask(task, isRun);
        return R.success();
    }
    
    /**
     * 开启或关闭动态任务
     *
     * @param id 动态任务ID
     */
    @GetMapping("/schedule/task/status")
    public R modifyStateDynamicTask(@RequestParam("id") Long id, @RequestParam("status") Boolean status) {
        if (Boolean.TRUE.equals(status)) {
            pluginService.startDynamicTask(id);
        } else {
            pluginService.stopDynamicTask(id);
        }
        return R.success();
    }
    
    /**
     * 从任务队列中移除或在数据库中删除一个动态任务
     *
     * @param id 定时任务ID
     */
    @DeleteMapping("/schedule/task/{id}")
    public R removeOrPauseDynamicTask(@PathVariable("id") Long id) {
        pluginService.removeOrPauseDynamicTask(id);
        return R.success();
    }
}
