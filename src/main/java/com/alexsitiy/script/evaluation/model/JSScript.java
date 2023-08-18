package com.alexsitiy.script.evaluation.model;

import java.io.ByteArrayOutputStream;

public class JSScript {
    private Integer id;
    private Status status;
    private Long executionTime;
    private String body;
    // TODO: 18.08.2023 Thread-safe outputStream
    private ByteArrayOutputStream result;
    private ByteArrayOutputStream errors;

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

    public ByteArrayOutputStream getErrors() {
        return errors;
    }

    public void setErrors(ByteArrayOutputStream errors) {
        this.errors = errors;
    }

    public JSScript(Integer id, Status status, Long executionTime, String body, ByteArrayOutputStream result, ByteArrayOutputStream errors) {
        this.id = id;
        this.status = status;
        this.executionTime = executionTime;
        this.body = body;
        this.result = result;
        this.errors = errors;
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
