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
 * for getBody() endpoint.
 *
 * @see com.alexsitiy.script.evaluation.doc.ScriptController
 * @see com.alexsitiy.script.evaluation.controller.ScriptRestController
 * */
@Operation(
        summary = "Obtains script's body by its id. It also includes caching via ETag header and gzip compression if the size is more than 1KB",
        parameters = {
                @Parameter(name = "id", in = ParameterIn.PATH, required = true)
        })
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "OK",
                headers = {
                        @Header(name = "Cache-Control: no-cache", description = "It's used for caching"),
                        @Header(name = "ETag", description = "It's used for cache validation")
                },
                content = @Content(
                        mediaType = "text/plain",
                        schema = @Schema(example = """
                                function calculateSum() {
                                    console.log('Test')
                                    console.error('some error')
                                   
                                   return 'test';
                                 }
                                """)
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
public @interface GetBodyApiEndpoint {
}
