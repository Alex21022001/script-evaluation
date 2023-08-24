package com.alexsitiy.script.evaluation.dto;

import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.hateoas.RepresentationModel;

public class JSScriptReadDto extends RepresentationModel<JSScriptFullReadDto> {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        JSScriptReadDto that = (JSScriptReadDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (status != that.status) return false;
        return executionTime != null ? executionTime.equals(that.executionTime) : that.executionTime == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (executionTime != null ? executionTime.hashCode() : 0);
        return result;
    }
}
