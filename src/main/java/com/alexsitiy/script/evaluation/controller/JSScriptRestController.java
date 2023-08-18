package com.alexsitiy.script.evaluation.controller;

import com.alexsitiy.script.evaluation.service.JSScriptExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scripts/js")
public class JSScriptRestController {

    private final JSScriptExecutionService jsScriptExecutionService;

    @Autowired
    public JSScriptRestController(JSScriptExecutionService jsScriptExecutionService) {
        this.jsScriptExecutionService = jsScriptExecutionService;
    }

    @GetMapping
    public ResponseEntity<?> findAll(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Integer id){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/evaluate")
    public ResponseEntity<?> evaluate(@RequestBody String jsCode){
        return ResponseEntity.ok(jsScriptExecutionService.evaluate(jsCode));
    }

    @PostMapping("/stop/{id}")
    public ResponseEntity<?> stop(@PathVariable Integer id){

        return ResponseEntity.ok().build();
    }
}
