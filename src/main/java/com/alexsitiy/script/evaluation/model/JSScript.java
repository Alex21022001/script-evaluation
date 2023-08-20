package com.alexsitiy.script.evaluation.model;

import java.io.ByteArrayOutputStream;

public class JSScript {
    private Integer id;
    private Status status;
    private Long executionTime;
    private String body;
    // TODO: 18.08.2023 Thread-safe outputStream
    private ByteArrayOutputStream result;
    private String errors;


    public String calculateExecutionTime() {
        return executionTime == null ? status == Status.IN_QUEUE ?
                "The code has not been executed yet" : "The code is currently running"
                : "%d MS".formatted(this.executionTime);
    }

    public String readResult() {
        return this.result.toString();
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

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
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
