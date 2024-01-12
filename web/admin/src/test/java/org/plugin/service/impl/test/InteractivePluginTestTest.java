package org.plugin.service.impl.test;

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
import org.plugin.service.impl.PluginPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web.admin.AdminSpringBootApplication;

import java.util.List;

@SpringBootTest(classes = {AdminSpringBootApplication.class, AdminSpringBootApplication.class})
@Slf4j
class InteractivePluginTestTest {
    
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
        InteractivePluginTest plugin = new InteractivePluginTest();
        List<PluginLabelValue> params = plugin.getParams();
        String input = "有何不可";
        String filter = "name";
        String type = "qq";
        String page = "1";
        
        params.parallelStream()
              .filter(pluginLabelValue -> StringUtils.equals(InteractivePluginTest.FILTER, pluginLabelValue.getKey()))
              .findFirst()
              .orElseThrow(RuntimeException::new)
              .setValue(filter);
        
        params.parallelStream()
              .filter(pluginLabelValue -> StringUtils.equals(InteractivePluginTest.TYPE, pluginLabelValue.getKey()))
              .findFirst()
              .orElseThrow(RuntimeException::new)
              .setValue(type);
        
        params.parallelStream()
              .filter(pluginLabelValue -> StringUtils.equals(InteractivePluginTest.PAGE, pluginLabelValue.getKey()))
              .findFirst()
              .orElseThrow(RuntimeException::new)
              .setValue(page);
        
        TbPluginTaskPojo taskPojo = pluginService.getTbPluginTaskPojo(441213820829829L, params, 424608186796165L);
        PluginPackage pluginPackage = new PluginPackage(musicFlowApi, pluginMsgService, pluginTaskService,
                qukuService, taskPojo.getId(), taskPojo.getUserId());
        List<PluginLabelValue> search = plugin.search(params, input);
        log.info(search.toString());
    }
}
