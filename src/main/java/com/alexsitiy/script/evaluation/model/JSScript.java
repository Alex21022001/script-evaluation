package com.alexsitiy.script.evaluation.model;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.atomic.AtomicLong;

public class JSScript {
    private Integer id;
    private Status status;
    private final AtomicLong executionTime = new AtomicLong(0);
    private String body;
    private ByteArrayOutputStream result;
    private volatile String errors;


    public String calculateExecutionTime() {
        return status == Status.IN_QUEUE ?
                "The code has not been executed yet" : status == Status.EXECUTING ?
                "The code is currently running" : "%d MS".formatted(executionTime.get());
    }

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
