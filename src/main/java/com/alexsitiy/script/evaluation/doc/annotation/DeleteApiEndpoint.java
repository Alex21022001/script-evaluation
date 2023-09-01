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
