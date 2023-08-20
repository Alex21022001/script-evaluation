package com.alexsitiy.script.evaluation.thread;

import com.alexsitiy.script.evaluation.event.JSScriptCompletionEvent;
import com.alexsitiy.script.evaluation.event.JSScriptExecutionEvent;
import com.alexsitiy.script.evaluation.model.JSScript;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.PolyglotException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.io.IOException;

public class JSScriptTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(JSScriptTask.class);

    private final JSScript jsScript;
    private ApplicationEventPublisher eventPublisher;
    private Context context;

    public JSScriptTask(JSScript jsScript, ApplicationEventPublisher eventPublisher) {
        this.jsScript = jsScript;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void run() {
        long executionTime;
        long start;
        try {
            context = Context.newBuilder()
                    .allowAllAccess(true)
                    .engine(Engine.newBuilder()
                            .option("engine.WarnInterpreterOnly", "false")
                            .build())
                    .out(jsScript.getResult())
                    .build();

            log.debug("Script {} is started", jsScript);
            eventPublisher.publishEvent(new JSScriptExecutionEvent(jsScript));

            start = System.currentTimeMillis();
            context.eval("js", jsScript.getBody());
            executionTime = System.currentTimeMillis() - start;

            eventPublisher.publishEvent(new JSScriptCompletionEvent(jsScript, executionTime));
            log.debug("Script {} is finished", jsScript);
        } catch (PolyglotException e) {
            // TODO: 18.08.2023 JSScriptFailedEvent
            if (e.isGuestException()) {
                log.debug("Script {}", e.getMessage());
            } else {
                log.debug("Internal Error: {}", e.getMessage());
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
}
