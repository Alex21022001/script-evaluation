package com.alexsitiy.script.evaluation.model;

import com.alexsitiy.script.evaluation.exception.ScriptNotValidException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.graalvm.polyglot.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The main model of the app that is used to run JavaScript code and to
 * store information about execution. It implements {@link Runnable} interface
 * that allows to run script in async way.
 * <br/>
 * It utilizes {@link Context} for running JavaScript code.
 */
public final class Script {

    private static final Logger log = LoggerFactory.getLogger(Script.class);
    private static final AtomicInteger idGenerator = new AtomicInteger();

    private final Integer id;
    private final AtomicReference<Status> status;
    private volatile Duration executionTime;
    private volatile Instant lastModified;
    private volatile Instant scheduledTime;
    private final String body;
    private final CircularLineBuffer result;

    private Value parsedCode;
    private Context context;
    private FutureTask<Void> task;

    /**
     * Creates an instance of {@link Script} with generated id. It also
     * initializes the following fields: status,result,context.
     *
     * @param body the JavaScript code that is needed to be executed.
     */
    private Script(String body, CircularLineBuffer result, Value parsedCode, Context context) {
        this.id = idGenerator.incrementAndGet();
        this.status = new AtomicReference<>(Status.IN_QUEUE);
        this.executionTime = Duration.ZERO;
        this.lastModified = Instant.now();
        this.body = body;
        this.result = result;
        this.parsedCode = parsedCode;
        this.context = context;
        this.task = new FutureTask<>(this::run, null);
    }

    /**
     * Creates an instance of {@link Script}, but validates the given JavaScript code
     * before.
     *
     * @param jsCode JavaScript that will be evaluated.
     * @return {@link Script}
     * @throws ScriptNotValidException if the given JavaScript code has some syntax errors and can't be executed.
     */
    public static Script create(String jsCode) {
        try {
            CircularLineBuffer result = new CircularLineBuffer(10, 100);
            Context context = createContext(result);

            Value parsed = context.parse("js", jsCode);

            return new Script(jsCode, result, parsed, context);
        } catch (PolyglotException e) {
            throw new ScriptNotValidException("The script has some syntax errors");
        }
    }

    /**
     * Runs a given JavaScript code via {@link Value} and changes
     * script's status, executionTime, lastModified fields during execution.
     */
    public void run() {
        long start = 0;

        try {
            log.debug("Script {} is started", this);
            setStatus(Status.IN_QUEUE, Status.EXECUTING);
            this.scheduledTime = Instant.now();

            start = System.currentTimeMillis();
            this.parsedCode.executeVoid();
            setExecutionTime(System.currentTimeMillis() - start);

            setStatus(Status.EXECUTING, Status.COMPLETED);
            log.debug("Script {} is completed successfully", this);
        } catch (PolyglotException e) {
            setExecutionTime(System.currentTimeMillis() - start);

            if (e.isGuestException()) {
                if (e.isInterrupted() || e.isCancelled()) {
                    setStatus(Status.EXECUTING, Status.INTERRUPTED);
                    log.debug("Script {} was interrupted", this);
                } else {
                    setStatus(Status.EXECUTING, Status.FAILED);
                    log.debug("Script {} is failed", this);
                }
            }
            this.result.write(("Error: " + ExceptionUtils.getStackTrace(e)).getBytes(StandardCharsets.UTF_8));
        } finally {
            releaseResources();
        }
    }


    /**
     * Terminates JavaScript code from executing or deletes it from
     * the thread pool's queue to release resources. Utilizes {@link FutureTask} to
     * do it.
     * <br/>
     * It also changes the script's status to INTERRUPTED and writes to stderr if
     * the task was deleted from the queue.
     */
    public void stop() {
        if (task != null && !task.isDone() && !task.isCancelled()) {
            task.cancel(true);
            context.close(true);

            if (this.status.compareAndSet(Status.IN_QUEUE, Status.INTERRUPTED)) {
                this.lastModified = Instant.now();
                this.result.write("Error: Script was deleted from the queue without execution".getBytes(StandardCharsets.UTF_8));
                releaseResources();
                log.debug("Script {} was deleted from the queue", this);
            }
        }
    }


    /**
     * Creates an instance of {@link Context} that will be used to
     * run JavaCode.
     * <br/>
     * It also includes {@link SandboxPolicy} as a CONSTRAINED in order to restrict
     * the running JavaScript code and make the app more independent.
     * <br/>
     * It utilizes {@link CircularLineBuffer} as a stdout and stderr that
     * ensures max capacity in order to alleviate the load on the heap.
     */
    private static Context createContext(OutputStream result) {
        return Context.newBuilder("js")
                .engine(Engine.newBuilder("js")
                        .option("engine.WarnInterpreterOnly", "false")
                        .sandbox(SandboxPolicy.CONSTRAINED)
                        .out(result)
                        .err(result)
                        .build())
                .build();
    }

    private void setStatus(Status expected, Status newStatus) {
        boolean success = this.status.compareAndSet(expected, newStatus);

        if (!success)
            throw new IllegalStateException("Expected status [%s] is not equal to the provided [%s]"
                    .formatted(expected.name(), newStatus.name()));

        this.lastModified = Instant.now();
    }

    private void releaseResources() {
        this.parsedCode = null;
        this.context = null;
        this.task = null;
    }

    private void setExecutionTime(long millis) {
        this.executionTime = Duration.of(millis, ChronoUnit.MILLIS);
    }

    public Runnable getTaskToBeRun() {
        return this.task;
    }

    public Instant getScheduledTime() {
        return scheduledTime;
    }

    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status.get();
    }

    public long getExecutionTime() {
        return executionTime.toMillis();
    }

    public String getBody() {
        return body;
    }

    public String getResult() {
        return result.toString();
    }


    public Instant getLastModified() {
        return lastModified;
    }

    @Override
    public String toString() {
        return "Script{" +
               "id=" + id +
               ", status=" + status +
               ", executionTime=" + this.getExecutionTime() +
               '}';
    }

}
