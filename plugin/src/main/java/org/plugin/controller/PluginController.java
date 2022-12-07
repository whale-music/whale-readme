package org.plugin.controller;

import org.plugin.service.sync.impl.music163.Music163SyncPluginImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController("plugins")
@RequestMapping("/admin")
public class PluginController {
    
    @Autowired
    private Music163SyncPluginImpl music163SyncPlugin;
    
    @PostMapping("/plugins")
    private String sync(@RequestParam("playId") String playId, @RequestParam("cookie") String cookie) throws IOException {
        return music163SyncPlugin.sync(playId, cookie, cookie).toString();
    }
}
