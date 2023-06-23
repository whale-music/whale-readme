package org.plugin.scheduling;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.core.mybatis.iservice.TbScheduleTaskService;
import org.core.mybatis.pojo.TbScheduleTaskPojo;
import org.plugin.service.PluginService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class TaskScheduling implements InitializingBean {
    
    private final TbScheduleTaskService scheduleService;
    
    private final DynamicTaskService dynamicTaskService;
    
    private final PluginService pluginService;
    
    public TaskScheduling(DynamicTaskService dynamicTaskService, TbScheduleTaskService scheduleService, PluginService pluginService) {
        this.dynamicTaskService = dynamicTaskService;
        this.scheduleService = scheduleService;
        this.pluginService = pluginService;
    }
    
    /**
     * 启动时自动读取数据库
     * 初始化定时任务
     */
    @Override
    public void afterPropertiesSet() {
        LambdaQueryWrapper<TbScheduleTaskPojo> wrapper = Wrappers.<TbScheduleTaskPojo>lambdaQuery().eq(TbScheduleTaskPojo::getStatus, true);
        List<TbScheduleTaskPojo> list = scheduleService.list(wrapper);
        log.info("init schedule 读取定时任务");
        for (TbScheduleTaskPojo tbScheduleTaskPojo : list) {
            dynamicTaskService.add(tbScheduleTaskPojo, pluginService);
            log.info("add schedule task 添加定时任务: {}", tbScheduleTaskPojo.getName());
        }
    }
}