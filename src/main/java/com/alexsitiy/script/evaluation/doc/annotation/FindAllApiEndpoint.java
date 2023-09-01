package com.alexsitiy.script.evaluation.doc.annotation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * This annotation is used for creating Swagger API documentation
 * for findAll() endpoint.
 *
 * @see com.alexsitiy.script.evaluation.doc.ScriptController
 * @see com.alexsitiy.script.evaluation.controller.ScriptRestController
 * */
@Operation(
        summary = "Gets all scripts, includes sorting and filtering",
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
                                schema = @Schema(example = """
                                        {
                                          "_embedded": {
                                            "scripts": [
                                              {
                                                  "id": 1,
                                                  "status": "IN_QUEUE",
                                                  "executionTime": 0,
                                                  "scheduledTime": null,
                                                  "_links": {
                                                    "self": [
                                                      {
                                                        "href": "http://localhost:8080/scripts/1"
                                                      },
                                                      {
                                                        "href": "http://localhost:8080/scripts/1/body",
                                                        "deprecation": "Gets the body of the script"
                                                      },
                                                      {
                                                        "href": "http://localhost:8080/scripts/1/result",
                                                        "deprecation": "Gets the result of the running script"
                                                      }
                                                    ],
                                                    "allScripts": {
                                                      "href": "http://localhost:8080/scripts?statuses=COMPLETED,EXECUTING&sorts=TIME,scheduled"
                                                    },
                                                    "stop": {
                                                      "href": "http://localhost:8080/scripts/1",
                                                      "type": "POST",
                                                      "deprecation": "Terminates the running script"
                                                    },
                                                    "delete": {
                                                      "href": "http://localhost:8080/scripts/1",
                                                      "type": "DELETE",
                                                      "deprecation": "Deletes a finished script"
                                                    }
                                                  }
                                                }
                                            ]
                                          }
                                        }
                                        """))
                }
        )
})
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FindAllApiEndpoint {
}
