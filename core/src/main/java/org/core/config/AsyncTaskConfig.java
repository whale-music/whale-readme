package org.core.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AsyncTaskConfig implements AsyncConfigurer {
    
    /**
     * 核心线程数
     */
//    @Value("${threadPool.corePoolSize}")
    private static final int CORE_POOL_SIZE = 10;
    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = 50;
    /**
     * 线程池缓冲队列容量
     */
    private static final int QUEUE_CAPACITY = 10;
    /**
     * 空闲线程销毁前等待时长
     */
    private static final int AWAIT_TERMINATION_SECONDS = 10;
    /**
     * 线程名前缀
     */
    private static final String THREAD_NAME_PREFIX = "Sample-Async-";
    
    /**
     * ThreadPoolTaskExcutor运行原理
     * 当线程池的线程数小于corePoolSize，则新建线程入池处理请求
     * 当线程池的线程数等于corePoolSize，则将任务放入Queue中，线程池中的空闲线程会从Queue中获取任务并处理
     * 当Queue中的任务数超过queueCapacity，则新建线程入池处理请求，但如果线程池线程数达到maxPoolSize，将会通过RejectedExecutionHandler做拒绝处理
     * 当线程池的线程数大于corePoolSize时，空闲线程会等待keepAliveTime长时间，如果无请求可处理就自行销毁
     */
    @Override
    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(CORE_POOL_SIZE);
        threadPool.setMaxPoolSize(MAX_POOL_SIZE);
        threadPool.setQueueCapacity(QUEUE_CAPACITY);
        threadPool.setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS);
        threadPool.setThreadNamePrefix(THREAD_NAME_PREFIX);
        
        // 关机时，是否等待任务执行完
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        // 设置拒绝策略
        // CALLER_RUNS：由调用者所在的线程执行该任务
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        
        // 初始化线程
        threadPool.initialize();
        return threadPool;
    }
    
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return null;
    }
}