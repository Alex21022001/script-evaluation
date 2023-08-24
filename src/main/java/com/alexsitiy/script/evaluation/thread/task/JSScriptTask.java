package com.alexsitiy.script.evaluation.thread.task;

import com.alexsitiy.script.evaluation.event.JSScriptCompletionEvent;
import com.alexsitiy.script.evaluation.event.JSScriptExecutionEvent;
import com.alexsitiy.script.evaluation.event.JSScriptFailureEvent;
import com.alexsitiy.script.evaluation.event.JSScriptInterruptedEvent;
import com.alexsitiy.script.evaluation.model.JSScript;
import com.alexsitiy.script.evaluation.model.Script;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.ResourceLimits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeoutException;


/**
 * The implementation of {@linkplain ScriptTask} for running JavaScript code obtained from {@linkplain JSScript}.
 * Use {@linkplain Context} to run JavaScript code.
 * Use {@linkplain ApplicationEventPublisher} to write error if any and change the current status of the {@linkplain JSScript}.
 *
 * @author Alex Sitiy
 * */
public class JSScriptTask implements ScriptTask {

    private static final Logger log = LoggerFactory.getLogger(JSScriptTask.class);

    private final JSScript jsScript;
    private final ApplicationEventPublisher eventPublisher;
    private Context context;

    public JSScriptTask(JSScript jsScript, ApplicationEventPublisher eventPublisher) {
        this.jsScript = jsScript;
        this.eventPublisher = eventPublisher;
    }

    /**
     * The implementation of {@linkplain Runnable} run()
     * */
    @Override
    public void run() {
        long executionTime;
        long start = 0;

        try {
            context = initContext();

            log.debug("Script {} is started", jsScript);
            eventPublisher.publishEvent(new JSScriptExecutionEvent(jsScript));

            start = System.currentTimeMillis();
            context.eval("js", jsScript.getBody());
            executionTime = System.currentTimeMillis() - start;

            eventPublisher.publishEvent(new JSScriptCompletionEvent(jsScript, executionTime));
            log.debug("Script {} is finished", jsScript);
        } catch (PolyglotException e) {
            executionTime = System.currentTimeMillis() - start;

            if (e.isGuestException()) {
                if (e.isInterrupted()) {
                    eventPublisher.publishEvent(new JSScriptInterruptedEvent(jsScript, executionTime, e.getMessage()));
                    log.debug("Script {} was interrupted", jsScript);
                } else {
                    eventPublisher.publishEvent(new JSScriptFailureEvent(jsScript, executionTime, e.getMessage()));
                    log.debug("Script {} failed", jsScript);
                }
            } else {
                log.error("Internal Error: {}", e.getMessage());
            }
        } finally {
            context.close();
            try {
                jsScript.getResult().close();
            } catch (IOException e) {
                log.error("Failed to close OutputStream");
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Terminate the task that is currently running in run()
     * */
    @Override
    public boolean stop() {
        try {
            context.interrupt(Duration.of(1, ChronoUnit.SECONDS));
            log.debug("Task was stopped");
        } catch (TimeoutException e) {
            context.close(true);
        }
        return true;
    }


    @Override
    public Script getScript() {
        return jsScript;
    }

    /**
     * Create instance of {@linkplain Context} that is used to run JS code
     * */
    private Context initContext() {
        return Context.newBuilder()
                .allowAllAccess(true)
                .engine(Engine.newBuilder()
                        .option("engine.WarnInterpreterOnly", "false")
                        .build())
                .out(jsScript.getResult())
                .resourceLimits(ResourceLimits.newBuilder()
//                        .statementLimit(1000,null)
                        .build())
                .build();
    }
}
