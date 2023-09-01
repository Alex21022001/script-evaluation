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
 * for getResult() endpoint.
 *
 * @see com.alexsitiy.script.evaluation.doc.ScriptController
 * @see com.alexsitiy.script.evaluation.controller.ScriptRestController
 * */
@Operation(
        summary = """
                Obtains script's result by its id.
                It also includes caching via Last-Modified header for scripts which status is one of INTERRUPTED,FAILED,COMPLETED
                and gzip compression if the size is more than 1KB.
                """,
        parameters = {
                @Parameter(name = "id", in = ParameterIn.PATH, required = true)
        })
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "OK",
                headers = {
                        @Header(name = "Last-Modified", description = "Returned when script is finished and can be cached"),
                        @Header(name = "Cache-Control: no-cache", description = "It's used for caching"),
                },
                content = @Content(
                        mediaType = "text/plain",
                        schema = @Schema(example = """
                                Test
                                some error
                                ReferenceError: a is not defined at <js> calculateSum2(Unnamed:10:178)
                                at <js> :program(Unnamed:22:320-334)
                                at org.graalvm.polyglot.Context.eval(Context.java:429)
                                at com.alexsitiy.script.evaluation.model.Script.run(Script.java:86)
                                at java.base/java.util.concurrent.CompletableFuture$AsyncRun.run(CompletableFuture.java:1804)
                                at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
                                at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
                                at java.base/java.lang.Thread.run(Thread.java:833)
                                """)
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
public @interface GetResultApiEndpoint {
}
