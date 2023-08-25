package com.alexsitiy.script.evaluation.listener;

import com.alexsitiy.script.evaluation.event.JSScriptInterruptedEvent;
import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * The listener that listens to {@link JSScriptInterruptedEvent}.
 *
 * @see Component
 * @see EventListener
 */
@Component
public class JSScriptInterruptedListener {

    /**
     *  Changes the {@link Status} of the {@link JSScript} to INTERRUPTED, sets its execution time
     *  and set obtained errors to the script object when the event occurs.
     *
     * @param event the occurred event.
     * @see com.alexsitiy.script.evaluation.thread.task.ScriptTask
     * */
    @EventListener
    public void onJSScriptExecution(JSScriptInterruptedEvent event){
        JSScript jsScript = (JSScript) event.getSource();
        jsScript.setStatus(Status.INTERRUPTED); // Here could be invoked JSScriptService/Repository
        jsScript.setExecutionTime(event.getExecutionTime());
        jsScript.setErrors(event.getError());
    }
}
