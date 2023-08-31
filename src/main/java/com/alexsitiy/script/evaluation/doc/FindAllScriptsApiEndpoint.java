package com.alexsitiy.script.evaluation.doc;

import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.hateoas.CollectionModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Operation(
        summary = "Gets all scripts, includes sorting and filtering",
        tags = {"script list"},
        parameters = {
                @Parameter(name = "statuses", description = "It's used for filtering", in = ParameterIn.QUERY),
                @Parameter(in = ParameterIn.QUERY, name = "sorts",
                        description = """
                                It's used for sorting. If the value is specified in lowercase than ASC sort will be use, uppercase - otherwise.
                                - id - sorts by script's id
                                - time - sorts by script's execution time
                                - scheduled - sorts by script's scheduled time                         
                                """,
                        schema = @Schema(type = "array", allowableValues = {"id,ID,time,TIME,scheduled,SCHEDULED"}),
                        examples = {
                                @ExampleObject(name = "Sort scripts by id in ASC", value = "id", summary = "id"),
                                @ExampleObject(name = "Sort scripts by id in DESC", value = "ID", summary = "ID"),
                                @ExampleObject(name = "Sort scripts by execution time in ASC", value = "time", summary = "time"),
                                @ExampleObject(name = "Sort scripts by execution time in DESC", value = "TIME", summary = "TIME"),
                                @ExampleObject(name = "Sort scripts by scheduled time in ASC", value = "scheduled", summary = "scheduled"),
                                @ExampleObject(name = "Sort scripts by scheduled time in DESC", value = "SCHEDULED", summary = "SCHEDULED")
                        })
        }
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "OK",
                content = {
                        @Content(
                                mediaType = "application/hal+json",
                                array = @ArraySchema(
                                        arraySchema = @Schema(implementation = CollectionModel.class),
                                        schema = @Schema(implementation = ScriptReadDto.class)))
                }
        )
})
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FindAllScriptsApiEndpoint {
}
