package org.plugin.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("plugins")
@RequestMapping("/admin")
public class PluginController {
    
    
    @GetMapping("/getAllPlugins")
    public String getAllPlugin(@RequestParam("userId") String userId) {
        return "";
    }
    
    
    @GetMapping("/getPluginParams")
    public String getPluginParams(@RequestParam("pluginId") String pluginId) {
        return "";
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
