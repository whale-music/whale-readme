package org.plugin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.service.MusicFlowApi;
import org.core.pojo.TbPluginTaskPojo;
import org.core.service.TbPluginMsgService;
import org.core.service.TbPluginTaskService;
import org.junit.jupiter.api.Test;
import org.plugin.converter.PluginLabelValue;
import org.plugin.service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web.MusicBoxSpringBoot;

import java.util.List;

@SpringBootTest(classes = MusicBoxSpringBoot.class)
@Slf4j
class SyncPluginTest {
    
    @Autowired
    private MusicFlowApi musicFlowApi;
    
    @Autowired
    private TbPluginMsgService pluginMsgService;
    
    @Autowired
    private TbPluginTaskService pluginTaskService;
    
    
    @Autowired
    private PluginService pluginService;
    
    @Test
    void testApply() {
        SyncPlugin plugin = new SyncPlugin();
        List<PluginLabelValue> params = plugin.getParams();
        String cookieStr = "MUSIC_U=d33658da9213990dece8c775a34a34c50a72fdf0cc97532e1e2f6d7efc8affd3519e07624a9f00535f3dd833cb266a5025ff223deb3065a43726809422c6334bdebf8de6ed45b634d4dbf082a8813684";
        String hostStr = "http://43.139.22.243:3000";
        String musicIdStr = "2023335778";
        String localUserIdStr = "403648304906373";
        
        params.parallelStream()
              .filter(pluginLabelValue -> StringUtils.equals(SyncPlugin.COOKIE_KEY, pluginLabelValue.getKey()))
              .findFirst()
              .orElseThrow()
              .setValue(cookieStr);
        
        params.parallelStream()
              .filter(pluginLabelValue -> StringUtils.equals(SyncPlugin.HOST_KEY, pluginLabelValue.getKey()))
              .findFirst()
              .orElseThrow()
              .setValue(hostStr);
        
        params.parallelStream()
              .filter(pluginLabelValue -> StringUtils.equals(SyncPlugin.MUSIC_ID_KEY, pluginLabelValue.getKey()))
              .findFirst()
              .orElseThrow()
              .setValue(musicIdStr);
        
        params.parallelStream()
              .filter(pluginLabelValue -> StringUtils.equals(SyncPlugin.USER_ID_KEY, pluginLabelValue.getKey()))
              .findFirst()
              .orElseThrow()
              .setValue(localUserIdStr);
        
        TbPluginTaskPojo taskPojo = pluginService.getTbPluginTaskPojo(405408338284677L, 403648304906373L);
        PluginPackage pluginPackage = new PluginPackage(musicFlowApi, pluginMsgService, pluginTaskService, taskPojo.getId(), taskPojo.getUserId(), plugin);
        plugin.apply(params, pluginPackage);
    }
}
