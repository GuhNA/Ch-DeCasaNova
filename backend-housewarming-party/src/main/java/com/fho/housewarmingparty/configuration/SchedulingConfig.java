package com.fho.housewarmingparty.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    private static final String THREAD_NAME_PREFIX = "scheduler";
    private static final int MAX_AWAIT_TERMINATION_SECONDS = 120;
    private static final int DEFAULT_POOL_SIZE = 5;

    @Bean(name = "dynamicTaskScheduler")
    protected TaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix(THREAD_NAME_PREFIX);
        scheduler.setAwaitTerminationSeconds(MAX_AWAIT_TERMINATION_SECONDS);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setPoolSize(DEFAULT_POOL_SIZE);
        scheduler.initialize();
        return scheduler;
    }
}
