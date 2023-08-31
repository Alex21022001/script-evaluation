package com.alexsitiy.script.evaluation.dto;

import com.alexsitiy.script.evaluation.model.Status;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.Instant;

@Relation(collectionRelation = "scripts",itemRelation = "script")
public class ScriptReadDto extends RepresentationModel<ScriptReadDto> {
    private final Integer id;
    private final Status status;
    private final Long executionTime;
    private final Instant scheduledTime;


    public ScriptReadDto(Integer id, Status status, Long executionTime, Instant scheduledTime) {
        this.id = id;
        this.status = status;
        this.executionTime = executionTime;
        this.scheduledTime = scheduledTime;
    }

    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public Instant getScheduledTime() {
        return scheduledTime;
    }
}
