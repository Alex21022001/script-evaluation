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
/**
 * This annotation is used for creating Swagger API documentation
 * for findById() endpoint.
 *
 * @see com.alexsitiy.script.evaluation.doc.ScriptController
 * @see com.alexsitiy.script.evaluation.controller.ScriptRestController
 * */
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
                responseCode = "401",
                description = "UNAUTHORIZED",
                headers = @Header(name = "WWW-Authenticate",schema = @Schema(example = "Bearer error=\"invalid_token\", error_description=\"An error occurred while attempting to decode the Jwt: Jwt expired at 2023-09-19T09:46:31Z\"")),
                content = @Content(schema = @Schema(hidden = true))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN",
                headers = @Header(name = "WWW-Authenticate",schema = @Schema(example = "Bearer error=\"insufficient_scope\",error_description=\"The request requires higher privileges than provided by the access token.\"")),
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
