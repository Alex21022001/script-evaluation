package com.alexsitiy.script.evaluation.listener;

import com.alexsitiy.script.evaluation.event.JSScriptCompletionEvent;
import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * The listener that listens to {@link JSScriptCompletionEvent}.
 * Change the {@link Status} of {@link JSScript} to COMPLETED and set its execution time
 * when the event occurs.
 *
 * @see com.alexsitiy.script.evaluation.thread.task.ScriptTask
 * @see Component
 * @see EventListener
 */
@Component
public class JSScriptCompletionListener {

    /**
     *  Changes the status of the script obtained  from event object
     *  to COMPLETED.
     *
     * @param event the occurred event.
     * @see JSScript
     * @see Status
     * */
    @EventListener
    public void onJSScriptExecution(JSScriptCompletionEvent event) {
        JSScript jsScript = (JSScript) event.getSource();
        jsScript.setStatus(Status.COMPLETED); // Here could be invoked JSScriptService/Repository
        jsScript.setExecutionTime(event.getExecutionTime());
    }
}
