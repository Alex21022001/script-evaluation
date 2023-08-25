package com.alexsitiy.script.evaluation.listener;

import com.alexsitiy.script.evaluation.event.JSScriptFailureEvent;
import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * The listener that listens to {@link JSScriptFailureEvent}.
 *
 *
 * @see com.alexsitiy.script.evaluation.thread.task.ScriptTask
 * @see Component
 * @see EventListener
 */
@Component
public class JSScriptFailureListener {

    /**
     *  Change the {@link Status} of {@link JSScript} to FAILED, set its execution time
     *  and set obtained errors to the script object when the event occurs.
     *
     * @param event the occurred event.
     * */
    @EventListener
    public void onJSScriptExecution(JSScriptFailureEvent event){
        JSScript jsScript = (JSScript) event.getSource();
        jsScript.setStatus(Status.FAILED); // Here could be invoked JSScriptService/Repository
        jsScript.setExecutionTime(event.getExecutionTime());
        jsScript.setErrors(event.getError());
    }
}
