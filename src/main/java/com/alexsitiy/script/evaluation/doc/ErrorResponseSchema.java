package com.alexsitiy.script.evaluation.doc;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * This class is used as a {@link Schema} for creating Swagger API
 * documentation and illustrates an example of possible error with 404(NOT_FOUND) status code.
 *
 * @see com.alexsitiy.script.evaluation.doc.annotation.FindByIdApiEndpoint
 * @see com.alexsitiy.script.evaluation.doc.annotation.GetResultApiEndpoint
 * @see com.alexsitiy.script.evaluation.doc.annotation.GetBodyApiEndpoint
 * @see com.alexsitiy.script.evaluation.doc.annotation.StopApiEndpoint
 * @see com.alexsitiy.script.evaluation.doc.annotation.DeleteApiEndpoint
 */
@Schema(example = """
        {
          "type": "about:blank",
          "title": "Script Not Found",
          "status": 404,
          "detail": "There is no such a Script with id:1",
          "instance": "/scripts/1",
          "scriptId": 1
        }
        """)
public class ErrorResponseSchema {
}
