package com.alexsitiy.script.evaluation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * This class is used for configuration of {@linkplain ThreadPoolTaskExecutor}
 * that is used as the main pool for running JavaScript code. It also uses
 * values such as {@code thread-capacity} and {@code queue-capacity} which are
 * specified in application.properties, default values will be use if they are not.
 */
@Configuration
public class ThreadPoolConfiguration {

    @Bean
    public TaskExecutor threadPoolTaskExecutor(@Value("${app.thread-pool.thread-capacity:2}") Integer poolSize,
                                               @Value("${app.thread-pool.queue-capacity:5}") Integer queueSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setQueueCapacity(queueSize);
        executor.initialize();
        return executor;
    }
}
