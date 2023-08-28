package com.alexsitiy.script.evaluation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * The configuration class that is used for
 * tuning WebMvc via implementing {@link WebMvcConfigurer}.
 * <br/>
 * It adds additional {@link HandlerMethodArgumentResolver} as {@link JSScriptSortHandlerMethod}.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JSScriptSortHandlerMethod jsScriptSortHandlerMethod;

    @Autowired
    public WebConfig(JSScriptSortHandlerMethod jsScriptSortHandlerMethod) {
        this.jsScriptSortHandlerMethod = jsScriptSortHandlerMethod;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jsScriptSortHandlerMethod);
    }

    @Bean
    public TaskExecutor threadPoolTaskExecutor(@Value("${app.thread-pool.thread-capacity}") Integer poolSize,
                                               @Value("${app.thread-pool.queue-capacity}") Integer queueSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.setQueueCapacity(queueSize);
        executor.initialize();
        return executor;
    }
}
