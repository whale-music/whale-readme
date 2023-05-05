package org.plugin.service.impl.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.service.MusicFlowApi;
import org.core.iservice.TbPluginMsgService;
import org.core.iservice.TbPluginTaskService;
import org.core.pojo.TbPluginTaskPojo;
import org.core.service.QukuService;
import org.junit.jupiter.api.Test;
import org.plugin.converter.PluginLabelValue;
import org.plugin.service.PluginService;
import org.plugin.service.impl.PluginPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web.MusicBoxSpringBoot;

import java.util.List;

@SpringBootTest(classes = MusicBoxSpringBoot.class)
@Slf4j
class InteractivePluginTestTest {
    
    @Autowired
    private MusicFlowApi musicFlowApi;
    
    @Autowired
    private TbPluginMsgService pluginMsgService;
    
    @Autowired
    private TbPluginTaskService pluginTaskService;
    
    @Autowired
    private QukuService qukuService;
    
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
        
        TbPluginTaskPojo taskPojo = pluginService.getTbPluginTaskPojo(405408338284677L, 403648304906373L);
        PluginPackage pluginPackage = new PluginPackage(musicFlowApi, pluginMsgService, pluginTaskService,
                qukuService, taskPojo.getId(), taskPojo.getUserId(), null);
        List<PluginLabelValue> search = plugin.search(params, input);
        log.info(search.toString());
    }
}
