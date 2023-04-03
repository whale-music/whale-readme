package org.plugin.controller;

import org.core.common.result.R;
import org.core.utils.UserUtil;
import org.plugin.model.res.PluginLabelValue;
import org.plugin.model.res.PluginReq;
import org.plugin.model.res.PluginRes;
import org.plugin.service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public R getAllPlugin(@RequestParam(value = "userId", required = false) Long userId, @RequestParam(value = "id", required = false) Long id) {
        List<PluginRes> list = pluginService.getAllPlugin(userId == null ? UserUtil.getUser().getId() : userId, id);
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
        List<PluginLabelValue> list = pluginService.getPluginParams(pluginId);
        return R.success(list);
    }
    
    /**
     * 运行插件任务
     *
     * @param pluginId 插件ID
     * @param req      插件入参
     */
    @GetMapping("/execPluginTask")
    public R execPluginTask(@RequestParam("pluginId") Long pluginId, List<PluginLabelValue> req) {
        pluginService.execPluginTask(pluginId, req);
        return R.success();
    }
    
    @GetMapping("/getPluginRuntimeTask")
    public String getPluginRuntimeTask(@RequestParam("userId") String userId) {
        return "";
    }
    
    @GetMapping("/getPluginRuntimeMessages")
    public String getPluginRuntimeMessages(@RequestParam("runtimeId") String runtimeId) {
        return "";
    }
}
