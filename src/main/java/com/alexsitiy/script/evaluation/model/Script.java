package com.alexsitiy.script.evaluation.model;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.ResourceLimits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Script implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Script.class);
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    private final Integer id;
    private volatile Status status;
    private volatile long executionTime;
    private volatile Instant scheduledTime;
    private final String body;
    private final ByteArrayOutputStream result;
    private final ByteArrayOutputStream errors;
    private volatile String stackTrace;

    private final Context context;

    public Script(String body) {
        this.id = idGenerator.incrementAndGet();
        this.status = Status.IN_QUEUE;
        this.body = body;
        this.result = new ByteArrayOutputStream();
        this.errors = new ByteArrayOutputStream();
        this.context = Context.newBuilder()
                .allowAllAccess(true)
                .engine(Engine.newBuilder()
                        .option("engine.WarnInterpreterOnly", "false")
                        .build())
                .err(this.errors)
                .out(this.result)
                .resourceLimits(ResourceLimits.newBuilder()
//                        .statementLimit(1000,null)
                        .build())
                .build();
    }

    public void stop() {
        try {
            this.context.interrupt(Duration.of(2, ChronoUnit.SECONDS));
        } catch (TimeoutException e) {
            this.context.close(true);
        }
    }

    @Override
    public void run() {
        long start = 0;

        try {
            log.debug("Script {} is started", this);
            this.status = Status.EXECUTING;
            this.scheduledTime = Instant.now();

            start = System.currentTimeMillis();
            context.eval("js", this.body);
            this.executionTime = System.currentTimeMillis() - start;

            this.status = Status.COMPLETED;
            log.debug("Script {} is completed successfully", this);
        } catch (PolyglotException e) {
            this.executionTime = System.currentTimeMillis() - start;

            if (e.isGuestException()) {
                if (e.isInterrupted()) {
                    this.status = Status.INTERRUPTED;
                    this.stackTrace = e.getMessage();
                    log.debug("Script {} was interrupted", this);
                } else {
                    this.status = Status.FAILED;
                    this.stackTrace = e.getMessage();
                    log.debug("Script {} is failed", this);
                }
            } else {
                log.error("Internal Error: {}", e.getMessage());
            }
        } finally {
            this.context.close();

            try {
                this.result.close();
                this.errors.close();
            } catch (IOException e) {
                log.error("Failed to close OutputStream");
            }
        }
    }

    public Instant getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Instant scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    public String getBody() {
        return body;
    }

    public ByteArrayOutputStream getResult() {
        return result;
    }

    public ByteArrayOutputStream getErrors() {
        return errors;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        return "Script{" +
               "id=" + id +
               ", status=" + status +
               ", executionTime=" + executionTime +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Script script = (Script) o;

        if (executionTime != script.executionTime) return false;
        if (id != null ? !id.equals(script.id) : script.id != null) return false;
        if (status != script.status) return false;
        if (scheduledTime != null ? !scheduledTime.equals(script.scheduledTime) : script.scheduledTime != null)
            return false;
        if (body != null ? !body.equals(script.body) : script.body != null) return false;
        if (result != null ? !result.equals(script.result) : script.result != null) return false;
        if (errors != null ? !errors.equals(script.errors) : script.errors != null) return false;
        if (stackTrace != null ? !stackTrace.equals(script.stackTrace) : script.stackTrace != null) return false;
        return context != null ? context.equals(script.context) : script.context == null;
    }

    @Override
    public int hashCode() {
        int result1 = id != null ? id.hashCode() : 0;
        result1 = 31 * result1 + (status != null ? status.hashCode() : 0);
        result1 = 31 * result1 + (int) (executionTime ^ (executionTime >>> 32));
        result1 = 31 * result1 + (scheduledTime != null ? scheduledTime.hashCode() : 0);
        result1 = 31 * result1 + (body != null ? body.hashCode() : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (errors != null ? errors.hashCode() : 0);
        result1 = 31 * result1 + (stackTrace != null ? stackTrace.hashCode() : 0);
        result1 = 31 * result1 + (context != null ? context.hashCode() : 0);
        return result1;
    }
}
