package org.plugin.controller;

import org.core.common.result.R;
import org.core.pojo.TbPluginMsgPojo;
import org.core.pojo.TbPluginTaskPojo;
import org.core.utils.UserUtil;
import org.plugin.model.res.PluginLabelValue;
import org.plugin.model.res.PluginMsgRes;
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
     */
    @PostMapping("/execPluginTask")
    public R execPluginTask(@RequestParam("pluginId") Long pluginId, @RequestParam(value = "onLine", required = false, defaultValue = "true") Boolean onLine) {
        TbPluginTaskPojo pojo = pluginService.getTbPluginTaskPojo(pluginId);
        if (Boolean.TRUE.equals(onLine)) {
            List<TbPluginMsgPojo> tbPluginMsgPojos = pluginService.onLineExecPluginTask(pluginId, pojo.getId());
            return R.success(tbPluginMsgPojos);
        } else {
            pluginService.execPluginTask(pluginId, onLine, pojo.getId());
            return R.success(pojo.getId());
        }
    }
    
    @GetMapping("/getPluginRuntimeTask")
    public String getPluginRuntimeTask(@RequestParam("userId") Long userId) {
        return "";
    }
    
    @GetMapping("/getPluginRuntimeMessages")
    public R getPluginRuntimeMessages(@RequestParam("runtimeId") Long runtimeId) {
        List<PluginMsgRes> list = pluginService.getPluginRuntimeMessages(runtimeId);
        return R.success(list);
    }
}
