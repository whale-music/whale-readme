package org.plugin.controller;

import org.core.common.result.R;
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
        pluginService.saveOrUpdatePlugin(req);
        return R.success();
    }
    
    @GetMapping("/getAllPlugins")
    public R getAllPlugin(@RequestParam("userId") Long userId) {
        List<PluginRes> list = pluginService.getAllPlugin(userId);
        return R.success(list);
    }
    
    /**
     * 查询插件入参
     *
     * @param pluginId 插件ID
     * @return 插件入参
     */
    @GetMapping("/getPluginParams")
    public R getPluginParams(@RequestParam("pluginId") String pluginId) {
        List<PluginLabelValue> list = pluginService.getPluginParams(pluginId);
        return R.success(list);
    }
    
    /**
     * 运行插件任务
     *
     * @param pluginId 插件ID
     * @param req      插件入参
     */
    @GetMapping("/getPluginParams")
    public R execPluginTask(@RequestParam("pluginId") String pluginId, List<PluginLabelValue> req) {
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
