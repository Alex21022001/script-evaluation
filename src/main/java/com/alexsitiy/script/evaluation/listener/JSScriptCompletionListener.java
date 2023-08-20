package com.alexsitiy.script.evaluation.listener;

import com.alexsitiy.script.evaluation.event.JSScriptCompletionEvent;
import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class JSScriptCompletionListener {

    @EventListener
    public void onJSScriptExecution(JSScriptCompletionEvent event){
        JSScript jsScript = (JSScript) event.getSource();
        jsScript.setStatus(Status.COMPLETED); // Here could be invoked JSScriptService/Repository
        jsScript.setExecutionTime(event.getExecutionTime());
    }
}
