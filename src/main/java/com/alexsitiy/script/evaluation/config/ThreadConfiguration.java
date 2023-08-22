package com.alexsitiy.script.evaluation.config;

import com.alexsitiy.script.evaluation.thread.ScriptThreadPool;
import com.alexsitiy.script.evaluation.thread.ScriptThreadPoolImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadConfiguration {

    private final Integer poolCapacity;
    private final Integer queueCapacity;

    public ThreadConfiguration(@Value("${app.thread-pool.thread-capacity:2}") Integer poolCapacity,
                               @Value("${app.thread-pool.queue-capacity:5}") Integer queueCapacity) {
        this.poolCapacity = poolCapacity;
        this.queueCapacity = queueCapacity;
    }

    @Bean(destroyMethod = "shutdown")
    public ScriptThreadPool scriptThreadPool() {
        return new ScriptThreadPoolImpl(poolCapacity, queueCapacity);
    }

}
