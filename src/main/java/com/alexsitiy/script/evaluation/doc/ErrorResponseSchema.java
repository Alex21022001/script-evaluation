package com.alexsitiy.script.evaluation.doc;

import io.swagger.v3.oas.annotations.media.Schema;

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
