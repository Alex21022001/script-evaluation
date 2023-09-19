package com.alexsitiy.script.evaluation.doc.annotation;

import com.alexsitiy.script.evaluation.doc.ErrorResponseSchema;
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
 * for delete() endpoint.
 *
 * @see com.alexsitiy.script.evaluation.doc.ScriptController
 * @see com.alexsitiy.script.evaluation.controller.ScriptRestController
 * */
@Operation(
        summary = "Deletes a finished script by its id",
        parameters = {
                @Parameter(name = "id", in = ParameterIn.PATH, required = true)
        }
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "No Content",
                headers = {
                        @Header(name = "Link",description = "It includes self link to the script")
                },
                content = @Content(
                        schema = @Schema(hidden = true)
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
public @interface DeleteApiEndpoint {
}
