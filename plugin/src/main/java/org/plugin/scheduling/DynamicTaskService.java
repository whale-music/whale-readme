package org.plugin.scheduling;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.core.pojo.TbPluginTaskPojo;
import org.core.pojo.TbScheduleTaskPojo;
import org.plugin.converter.PluginLabelValue;
import org.plugin.service.PluginService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class DynamicTaskService {
    
    /**
     * 以下两个都是线程安全的集合类。
     */
    public final Map<Long, ScheduledFuture<?>> taskMap = new ConcurrentHashMap<>();
    public final List<Long> taskList = new CopyOnWriteArrayList<>();
    private final ThreadPoolTaskScheduler syncScheduler;
    
    
    public DynamicTaskService(ThreadPoolTaskScheduler syncScheduler) {
        this.syncScheduler = syncScheduler;
    }
    
    /**
     * 查看添加到任务队列中的任务
     *
     * @return 返回任务
     */
    public List<Long> getSchedulerTaskList() {
        return taskList;
    }
    
    
    /**
     * 添加一个动态任务
     *
     * @param task 定时任务参数
     * @return 是否执行成功
     */
    public boolean add(TbScheduleTaskPojo task, PluginService pluginService) {
        if (Boolean.FALSE.equals(task.getStatus())) {
            return false;
        }
        // 此处的逻辑是 ，如果当前已经有这个名字的任务存在，先删除之前的，再添加现在的。（即重复就覆盖）
        if (null != taskMap.get(task.getId())) {
            stop(task.getId());
        }
        
        // schedule :调度给定的Runnable ，在指定的执行时间调用它。
        // 一旦调度程序关闭或返回的ScheduledFuture被取消，执行将结束。
        // 参数：
        // 任务 – 触发器触发时执行的 Runnable
        // trigger – 任务的cron
        ScheduledFuture<?> schedule = syncScheduler.schedule(getRunnable(task, pluginService),
                triggerContext -> new CronTrigger(task.getCron()).nextExecutionTime(triggerContext));
        taskMap.put(task.getId(), schedule);
        taskList.add(task.getId());
        return true;
    }
    
    
    /**
     * 运行任务
     *
     * @param task 运行参数
     */
    public Runnable getRunnable(TbScheduleTaskPojo task, PluginService pluginService) {
        return () -> {
            StopWatch stopWatch = new StopWatch();
            // 开始时间
            stopWatch.start();
            log.info("------start------");
            log.info("---动态定时任务运行---");
            log.info("task设定cron==> {}", task.getCron());
            log.info("开始时间==> {}", LocalDateTime.now());
            log.info("params: {}", task.getParams());
            List<PluginLabelValue> pluginLabelValue = JSON.parseObject(task.getParams(), new TypeReference<List<PluginLabelValue>>() {
            });
            TbPluginTaskPojo pojo = pluginService.getTbPluginTaskPojo(task.getPluginId(), pluginLabelValue, task.getUserId());
            pluginService.execPluginTask(pluginLabelValue, task.getPluginId(), false, pojo);
            stopWatch.stop();
            // 结束时间
            log.info("结束时间==> {}", LocalDateTime.now());
            log.info("执行时长: {} 毫秒", stopWatch.getTime(TimeUnit.MILLISECONDS));
            log.info("------end------");
        };
    }
    
    /**
     * 从任务队列中删除任务
     *
     * @param id 定时任务ID
     * @return 是否执行成功
     */
    public boolean stop(Long id) {
        if (null == taskMap.get(id)) {
            return false;
        }
        ScheduledFuture<?> scheduledFuture = taskMap.get(id);
        scheduledFuture.cancel(true);
        taskMap.remove(id);
        taskList.remove(id);
        return true;
    }
}