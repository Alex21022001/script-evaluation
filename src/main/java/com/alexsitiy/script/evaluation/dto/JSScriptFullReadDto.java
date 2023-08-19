package com.alexsitiy.script.evaluation.dto;

import com.alexsitiy.script.evaluation.model.Status;

public class JSScriptFullReadDto {
    private Integer id;
    private Status status;
    private String executionTime;
    private String body;
    private String result;
    private String errors;

    public JSScriptFullReadDto(Integer id, Status status, String executionTime, String body, String result, String errors) {
        this.id = id;
        this.status = status;
        this.executionTime = executionTime;
        this.body = body;
        this.result = result;
        this.errors = errors;
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

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
