package com.alexsitiy.script.evaluation.dto;

import com.alexsitiy.script.evaluation.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.Instant;
import java.util.Objects;

/**
 * The representation of {@link com.alexsitiy.script.evaluation.model.Script}
 * that is used as a response to user. It also extends {@link RepresentationModel}
 * to include HATEOAS links.
 * <br/>
 * It includes {@link Schema} annotation to show a possible example in Swagger API documentation.
 *
 * @see com.alexsitiy.script.evaluation.mapper.ScriptReadMapper
 */
@Schema(example = """
        {
           "id": 1,
           "status": "IN_QUEUE",
           "executionTime": 0,
           "scheduledTime": null,
           "_links": {
             "self": [
               {
                 "href": "http://localhost:8080/scripts/1"
               }
             ]
           }
        }
        """)
@Relation(collectionRelation = "scripts", itemRelation = "script")
@SuppressWarnings("unused")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ScriptReadDto that = (ScriptReadDto) o;

        if (!Objects.equals(id, that.id)) return false;
        if (status != that.status) return false;
        if (!Objects.equals(executionTime, that.executionTime))
            return false;
        return Objects.equals(scheduledTime, that.scheduledTime);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (executionTime != null ? executionTime.hashCode() : 0);
        result = 31 * result + (scheduledTime != null ? scheduledTime.hashCode() : 0);
        return result;
    }
}
