package com.alexsitiy.script.evaluation.model;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.PolyglotException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class Script implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Script.class);
    private static final AtomicInteger idGenerator = new AtomicInteger(0);

    private final Integer id;
    private final AtomicReference<Status> status;
    private volatile long executionTime;
    private volatile Instant scheduledTime;
    private final String body;
    private final CyclicOutputStream result;
    private final ByteArrayOutputStream errors;
    private volatile long lastModified;

    private CompletableFuture<Void> task;
    private final Context context;

    public Script(String body) {
        this.id = idGenerator.incrementAndGet();
        this.status = new AtomicReference<>(Status.IN_QUEUE);
        this.body = body;
        // TODO: 28.08.2023 Use one instance for result and errors
        this.result = new CyclicOutputStream(1024);
        this.errors = new ByteArrayOutputStream();
        this.context = createContext();
    }

    private Context createContext() {
        return Context.newBuilder("js")
                .allowAllAccess(true)
                .engine(Engine.newBuilder()
                        .option("engine.WarnInterpreterOnly", "false")
                        .build())
                .err(this.errors)
                .out(this.result)
                .build();
    }

    public void stop() {
        if (!task.isDone()) {
            task.cancel(true);
            try {
                context.interrupt(Duration.of(1, ChronoUnit.SECONDS));
            } catch (TimeoutException e) {
                context.close(true);
            } finally {
                if (this.status.get() == Status.IN_QUEUE) {
                    this.status.set(Status.INTERRUPTED);
                    this.lastModified = Instant.now().toEpochMilli();
                    this.errors.writeBytes("Script was deleted from the queue without execution".getBytes(StandardCharsets.UTF_8));
                }
                log.debug("Script {} was forcibly closed", this);
            }
        }
    }

    @Override
    public void run() {
        long start = 0;

        try {
            log.debug("Script {} is started", this);
            this.status.set(Status.EXECUTING);
            this.scheduledTime = Instant.now();

            start = System.currentTimeMillis();
            context.eval("js", this.body);
            this.executionTime = System.currentTimeMillis() - start;

            this.status.set(Status.COMPLETED);
            this.lastModified = Instant.now().toEpochMilli();
            log.debug("Script {} is completed successfully", this);
        } catch (PolyglotException e) {
            this.executionTime = System.currentTimeMillis() - start;
            this.lastModified = Instant.now().toEpochMilli();

            if (e.isGuestException()) {
                if (e.isInterrupted()) {
                    this.status.set(Status.INTERRUPTED);
                    this.errors.writeBytes(e.getMessage().getBytes(StandardCharsets.UTF_8));
                    log.debug("Script {} was interrupted", this);
                } else {
                    // TODO: 28.08.2023 Convert stackTrace to OutputStream
                    this.status.set(Status.FAILED);
                    this.errors.writeBytes(e.getMessage().getBytes(StandardCharsets.UTF_8));
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

    public void setTask(CompletableFuture<Void> task) {
        this.task = task;
    }

    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status.get();
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

    public OutputStream getResult() {
        return result;
    }

    public ByteArrayOutputStream getErrors() {
        return errors;
    }

    public long getLastModified() {
        return lastModified;
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
        if (!Objects.equals(id, script.id)) return false;
        if (status != script.status) return false;
        if (!Objects.equals(scheduledTime, script.scheduledTime))
            return false;
        if (!Objects.equals(body, script.body)) return false;
        if (!Objects.equals(result, script.result)) return false;
        if (!Objects.equals(errors, script.errors)) return false;
        return Objects.equals(context, script.context);
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
        result1 = 31 * result1 + (context != null ? context.hashCode() : 0);
        return result1;
    }
}
