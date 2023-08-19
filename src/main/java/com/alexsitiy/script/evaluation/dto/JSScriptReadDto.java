package com.alexsitiy.script.evaluation.dto;

import com.alexsitiy.script.evaluation.model.Status;

public class JSScriptReadDto {
    private Integer id;
    private Status status;
    private String executionTime;

    public JSScriptReadDto(Integer id, Status status, String executionTime) {
        this.id = id;
        this.status = status;
        this.executionTime = executionTime;
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
}
