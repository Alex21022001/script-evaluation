package com.alexsitiy.script.evaluation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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
}
