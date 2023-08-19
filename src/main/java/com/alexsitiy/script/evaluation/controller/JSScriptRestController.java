package com.alexsitiy.script.evaluation.controller;

import com.alexsitiy.script.evaluation.dto.JSScriptFullReadDto;
import com.alexsitiy.script.evaluation.dto.JSScriptReadDto;
import com.alexsitiy.script.evaluation.model.JSScriptFilter;
import com.alexsitiy.script.evaluation.model.JSScriptSort;
import com.alexsitiy.script.evaluation.service.JSScriptExecutionService;
import com.alexsitiy.script.evaluation.service.JSScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scripts/js")
public class JSScriptRestController {

    private final JSScriptExecutionService jsScriptExecutionService;
    private final JSScriptService jsService;

    @Autowired
    public JSScriptRestController(JSScriptExecutionService jsScriptExecutionService, JSScriptService jsService) {
        this.jsScriptExecutionService = jsScriptExecutionService;
        this.jsService = jsService;
    }

    @GetMapping
    public ResponseEntity<List<JSScriptReadDto>> findAll(JSScriptFilter filter, JSScriptSort sort) {
        return ResponseEntity.ok(jsService.findAll(filter,sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JSScriptFullReadDto> findById(@PathVariable Integer id) {
        return jsService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping("/evaluate")
    public ResponseEntity<JSScriptFullReadDto> evaluate(@RequestBody String jsCode) {
        return ResponseEntity.ok(jsScriptExecutionService.evaluate(jsCode));
    }

    @PostMapping("/stop/{id}")
    public ResponseEntity<?> stop(@PathVariable Integer id) {

        return ResponseEntity.ok().build();
    }
}
