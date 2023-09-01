package com.alexsitiy.script.evaluation.doc.annotation;

import com.alexsitiy.script.evaluation.doc.ErrorResponseSchema;
import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Operation(
        summary = "Gets a specific script by its id. It also includes caching via Last-Modified header.",
        parameters = {
                @Parameter(name = "id", in = ParameterIn.PATH, required = true)
        })
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "OK",
                headers = {
                        @Header(name = "Last-Modified", description = "It's used to validate cache"),
                        @Header(name = "Cache-Control: no-cache", description = "It's used for caching"),
                },
                content = @Content(
                        mediaType = "application/hal+json",
                        schema = @Schema(implementation = ScriptReadDto.class)
                )
        ),
        @ApiResponse(
                responseCode = "304",
                description = "Not Modified",
                content = @Content(schema = @Schema(hidden = true))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Script Not Found by a specified id",
                content = @Content(
                        mediaType = "application/problem+json",
                        schema = @Schema(implementation = ErrorResponseSchema.class)
                )
        )
})
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FindByIdApiEndpoint {
}
