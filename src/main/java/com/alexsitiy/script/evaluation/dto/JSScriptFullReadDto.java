package com.alexsitiy.script.evaluation.dto;

import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.hateoas.RepresentationModel;

/**
 *  The representation of {@link com.alexsitiy.script.evaluation.model.JSScript} which
 *  possess all data of the script. It's used to inform user. The class extends {@link RepresentationModel}
 *  that used for HATEOAS implementation.
 *
 * @see com.alexsitiy.script.evaluation.mapper.JSScriptFullReadMapper
 * @see com.alexsitiy.script.evaluation.service.JSScriptService
 * */
public class JSScriptFullReadDto extends RepresentationModel<JSScriptFullReadDto> {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        JSScriptFullReadDto that = (JSScriptFullReadDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (status != that.status) return false;
        if (executionTime != null ? !executionTime.equals(that.executionTime) : that.executionTime != null)
            return false;
        if (body != null ? !body.equals(that.body) : that.body != null) return false;
        if (result != null ? !result.equals(that.result) : that.result != null) return false;
        return errors != null ? errors.equals(that.errors) : that.errors == null;
    }

    @Override
    public int hashCode() {
        int result1 = super.hashCode();
        result1 = 31 * result1 + (id != null ? id.hashCode() : 0);
        result1 = 31 * result1 + (status != null ? status.hashCode() : 0);
        result1 = 31 * result1 + (executionTime != null ? executionTime.hashCode() : 0);
        result1 = 31 * result1 + (body != null ? body.hashCode() : 0);
        result1 = 31 * result1 + (result != null ? result.hashCode() : 0);
        result1 = 31 * result1 + (errors != null ? errors.hashCode() : 0);
        return result1;
    }
}
