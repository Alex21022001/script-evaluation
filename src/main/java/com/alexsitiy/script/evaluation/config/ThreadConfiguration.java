package com.alexsitiy.script.evaluation.config;

import com.alexsitiy.script.evaluation.thread.JSScriptTask;
import com.alexsitiy.script.evaluation.thread.JSThreadPool;
import com.alexsitiy.script.evaluation.thread.ScriptThreadPool;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadConfiguration {

    private final Integer poolCapacity;
    private final Integer queueCapacity;

    public ThreadConfiguration(@Value("${app.thread-pool.queue-capacity:2}") Integer poolCapacity,
                               @Value("${app.thread-pool.thread-capacity:5}") Integer queueCapacity) {
        this.poolCapacity = poolCapacity;
        this.queueCapacity = queueCapacity;
    }

    @Bean(destroyMethod = "shutdown")
    public ScriptThreadPool<JSScriptTask, Integer> scriptThreadPool() {
        return new JSThreadPool(poolCapacity, queueCapacity);
    }

    @PreDestroy
    public void shutdown(ScriptThreadPool scriptThreadPool){
        scriptThreadPool.shutdown();
    }
}
