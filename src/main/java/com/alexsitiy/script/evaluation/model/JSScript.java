package com.alexsitiy.script.evaluation.model;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The implementation of {@linkplain Script} that is used for running JavaScript code.
 * It includes: id, {@linkplain Status}, executionTime, body, result, errors.
 *
 * @see com.alexsitiy.script.evaluation.thread.task.ScriptTask
 */
public class JSScript implements Script {
    private Integer id;
    private Status status;
    private final AtomicLong executionTime = new AtomicLong(0);
    private String body;
    private ByteArrayOutputStream result;
    private volatile String errors;


    /**
     * Calculates execution time of the script according to its status.
     *
     * @return {@link String} the string representation of execution time.
     */
    public String calculateExecutionTime() {
        return status == Status.IN_QUEUE ?
                "The code has not been executed yet" : status == Status.EXECUTING ?
                "The code is currently running" : "%d MS".formatted(executionTime.get());
    }

    /**
     * Reads all accumulated bytes inside result field and represents
     * it as a result of the script.
     *
     * @return {@link String} - the result of the script's evaluation.
     * @see ByteArrayOutputStream
     */
    public String readResult() {
        return this.result.toString();
    }

    public Long getExecutionTime() {
        return executionTime.get();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime.set(executionTime);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public ByteArrayOutputStream getResult() {
        return result;
    }

    public void setResult(ByteArrayOutputStream result) {
        this.result = result;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    public JSScript(Status status, String body, ByteArrayOutputStream result) {
        this.status = status;
        this.body = body;
        this.result = result;
    }

    @Override
    public String toString() {
        return "JSScript{" +
               "id=" + id +
               ", status=" + status +
               ", executionTime=" + executionTime +
               '}';
    }
}
