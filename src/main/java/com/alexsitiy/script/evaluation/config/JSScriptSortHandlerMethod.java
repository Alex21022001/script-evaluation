package com.alexsitiy.script.evaluation.config;

import com.alexsitiy.script.evaluation.model.JSScriptSort;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Array;
import java.util.Arrays;

@Component
public class JSScriptSortHandlerMethod implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == JSScriptSort.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String mayBeSort = webRequest.getParameter("sort");
        JSScriptSort jsScriptSort = new JSScriptSort();

        if (mayBeSort != null) {
            jsScriptSort.addAll(Arrays.asList(mayBeSort.split(",")));
        }

        return jsScriptSort;
    }
}
