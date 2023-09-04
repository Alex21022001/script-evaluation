package com.alexsitiy.script.evaluation.model;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.SandboxPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The main model of the app that is used to run JavaScript code and to
 * store information about execution. It implements {@link Runnable} interface
 * that allows to run script in async way.
 * <br/>
 * It utilizes {@link Context} for running JavaScript code.
 */
public final class Script implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(Script.class);
    private static final AtomicInteger idGenerator = new AtomicInteger();

    private final Integer id;
    private final AtomicReference<Status> status;
    private volatile long executionTime;
    private volatile long lastModified;
    private volatile Instant scheduledTime;
    private final String body;
    private final CyclicByteArrayOutputStream result;

    private CompletableFuture<Void> task;
    private Context context;

    /**
     * Creates an instance of {@link Script} with generated id. It also
     * initializes the following fields: status,result,context.
     *
     * @param body the JavaScript code that is needed to be executed.
     */
    public Script(String body) {
        this.id = idGenerator.incrementAndGet();
        this.status = new AtomicReference<>(Status.IN_QUEUE);
        this.body = body;
        this.result = new CyclicByteArrayOutputStream(2048);
        this.context = createContext();
    }

    /**
     * Creates an instance of {@link Context} that will be used to
     * run JavaCode.
     * <br/>
     * It also includes {@link SandboxPolicy} as a CONSTRAINED in order to restrict
     * the running JavaScript code and make the app more independent.
     * <br/>
     * It utilizes {@link CyclicByteArrayOutputStream} as a stdout and stderr that
     * ensures max capacity in order to alleviate the load on the heap.
     */
    private Context createContext() {
        return Context.newBuilder("js")
                .engine(Engine.newBuilder("js")
                        .option("engine.WarnInterpreterOnly", "false")
                        .sandbox(SandboxPolicy.CONSTRAINED)
                        .out(result)
                        .err(result)
                        .build())
                .build();
    }

    /**
     * Terminates JavaScript code from executing or deletes it from
     * the thread pool's queue to release resources. Utilizes {@link CompletableFuture} to
     * do it.
     * <br/>
     * It also changes the script's status to INTERRUPTED and writes to stderr if
     * the task was deleted from the queue.
     */
    public void stop() {
        if (task != null && !task.isDone()) {
            task.cancel(true);
            context.close(true);

            if (this.status.get() == Status.IN_QUEUE) {
                this.status.set(Status.INTERRUPTED);
                this.lastModified = Instant.now().toEpochMilli();
                this.result.write("Error: Script was deleted from the queue without execution".getBytes(StandardCharsets.UTF_8));
                releaseResources();
                log.debug("Script {} was deleted from the queue", this);
            }
        }
    }

    /**
     * Runs a given JavaScript code via {@link Context} and changes
     * script's status, executionTime, lastModified fields during execution.
     */

    @Override
    public void run() {
        long start = 0;

        try {
            log.debug("Script {} is started", this);
            this.status.set(Status.EXECUTING);
            this.scheduledTime = Instant.now();
            this.lastModified = Instant.now().toEpochMilli();

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
                if (e.isInterrupted() || e.isCancelled()) {
                    this.status.set(Status.INTERRUPTED);
                    log.debug("Script {} was interrupted", this);
                } else {
                    this.status.set(Status.FAILED);
                    log.debug("Script {} is failed", this);
                }
            }
            this.result.write(("Error: " + ExceptionUtils.getStackTrace(e)).getBytes(StandardCharsets.UTF_8));
        } finally {
            this.context.close();
            releaseResources();
        }
    }

    private void releaseResources() {
        this.context = null;
        this.task = null;
    }

    public Instant getScheduledTime() {
        return scheduledTime;
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

    public String getBody() {
        return body;
    }

    public String getResult() {
        return result.toString();
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
        result1 = 31 * result1 + (context != null ? context.hashCode() : 0);
        return result1;
    }
}
