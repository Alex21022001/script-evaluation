package com.alexsitiy.script.evaluation.doc;

import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Operation(
        summary = "Gets a specific script by its id",
        parameters = {
                @Parameter(name = "id", in = ParameterIn.PATH, required = true)
        })
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(
                        mediaType = "application/hal+json",
                        schema = @Schema(implementation = ScriptReadDto.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Script Not Found by a specified id",
                content = @Content(
                        mediaType = "application/problem+json",
                        schema = @Schema(example = """
                                {
                                  "type": "about:blank",
                                  "title": "Script Not Found",
                                  "status": 404,
                                  "detail": "There is no such a Script with id:1",
                                  "instance": "/scripts/1",
                                  "scriptId": 1
                                }
                                """)
                )
        )
})
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FindByIdApiEndpoint {
}
