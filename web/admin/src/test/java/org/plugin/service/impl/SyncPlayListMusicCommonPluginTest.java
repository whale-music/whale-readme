package org.plugin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.service.MusicFlowApi;
import org.api.common.service.QukuAPI;
import org.core.mybatis.iservice.TbPluginMsgService;
import org.core.mybatis.iservice.TbPluginTaskService;
import org.core.mybatis.pojo.TbPluginTaskPojo;
import org.junit.jupiter.api.Test;
import org.plugin.converter.PluginLabelValue;
import org.plugin.service.PluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web.admin.AdminSpringBootApplication;

import java.util.List;

@SpringBootTest(classes = AdminSpringBootApplication.class)
@Slf4j
class SyncPlayListMusicCommonPluginTest {
    
    @Autowired
    private MusicFlowApi musicFlowApi;
    
    @Autowired
    private TbPluginMsgService pluginMsgService;
    
    @Autowired
    private TbPluginTaskService pluginTaskService;
    
    @Autowired
    private QukuAPI qukuService;
    
    @Autowired
    private PluginService pluginService;
    
    @Test
    void testApply() {
        SyncPlayListMusicPlugin plugin = new SyncPlayListMusicPlugin();
        List<PluginLabelValue> params = plugin.getParams();
        String cookieStr = "";
        String hostStr = "";
        String playListIdStr = "";
        String localUserIdStr = "";
        
        params.parallelStream()
              .filter(pluginLabelValue -> StringUtils.equals(SyncPlayListMusicPlugin.COOKIE_KEY, pluginLabelValue.getKey()))
              .findFirst()
              .orElseThrow(RuntimeException::new)
              .setValue(cookieStr);
        
        params.parallelStream()
              .filter(pluginLabelValue -> StringUtils.equals(SyncPlayListMusicPlugin.HOST_KEY, pluginLabelValue.getKey()))
              .findFirst()
              .orElseThrow(RuntimeException::new)
              .setValue(hostStr);
        
        params.parallelStream()
              .filter(pluginLabelValue -> StringUtils.equals(SyncPlayListMusicPlugin.PlAY_LIST_ID_KEY, pluginLabelValue.getKey()))
              .findFirst()
              .orElseThrow(RuntimeException::new)
              .setValue(playListIdStr);
        
        params.parallelStream()
              .filter(pluginLabelValue -> StringUtils.equals(SyncPlayListMusicPlugin.USER_ID_KEY, pluginLabelValue.getKey()))
              .findFirst()
              .orElseThrow(RuntimeException::new)
              .setValue(localUserIdStr);
        
        TbPluginTaskPojo taskPojo = pluginService.getTbPluginTaskPojo(405408338284677L, params, 403648304906373L);
        PluginPackage pluginPackage = new PluginPackage(musicFlowApi, pluginMsgService, pluginTaskService,
                qukuService, taskPojo.getId(), taskPojo.getUserId());
        plugin.apply(params, pluginPackage);
    }
}
