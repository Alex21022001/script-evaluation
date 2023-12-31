package com.alexsitiy.script.evaluation.doc.annotation;

import com.alexsitiy.script.evaluation.dto.ScriptReadDto;
import com.alexsitiy.script.evaluation.dto.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ProblemDetail;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * This annotation is used for creating Swagger API documentation
 * for evaluate() endpoint.
 *
 * @see com.alexsitiy.script.evaluation.doc.ScriptController
 * @see com.alexsitiy.script.evaluation.controller.ScriptRestController
 * */
@Operation(
        summary = "Evaluates a given JavaScript code, but before it validates the script",
        requestBody = @RequestBody(required = true, description = "JavaScript code that will be evaluated",
                content = {
                        @Content(mediaType = "text/plain", schema = @Schema(example = "console.log('Test');")),
                }
        )
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "202",
                description = "ACCEPTED",
                content = @Content(
                        mediaType = "application/hal+json",
                        schema = @Schema(implementation = ScriptReadDto.class)
                )
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
                responseCode = "400",
                description = "Script has some syntax errors",
                content = @Content(
                        mediaType = "application/problem+json",
                        schema = @Schema(implementation = ValidationErrorResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "429",
                description = "Too Many Requests",
                content = @Content(
                        mediaType = "application/problem+json",
                        schema = @Schema(implementation = ProblemDetail.class)
                )
        )
})
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EvaluateApiEndpoint {
}
