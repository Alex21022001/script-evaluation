package com.alexsitiy.script.evaluation.doc.annotation;

import com.alexsitiy.script.evaluation.dto.UserCreateDto;
import com.alexsitiy.script.evaluation.dto.UserReadDto;
import com.alexsitiy.script.evaluation.dto.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * This annotation is used for creating Swagger API documentation
 * for register() endpoint.
 *
 * @see com.alexsitiy.script.evaluation.controller.AuthController
 */
@Operation(
        summary = "Creates and adds a new User to Keycloak authorization server",
        requestBody = @RequestBody(required = true, description = "User data",
                content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = UserCreateDto.class)),
                }
        )
)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "CREATED",
                content = @Content(
                        mediaType = "application/hal+json",
                        schema = @Schema(implementation = UserReadDto.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid user data",
                content = @Content(
                        mediaType = "application/problem+json",
                        schema = @Schema(implementation = ValidationErrorResponse.class)
                )
        )
})
@Target({METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterUserApiEndpoint {
}
