package com.alexsitiy.script.evaluation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/js/scripts")
public class JSScriptRestController {

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

        return ResponseEntity.ok().build();
    }

    @PostMapping("/stop/{id}")
    public ResponseEntity<?> stop(@PathVariable Integer id){

        return ResponseEntity.ok().build();
    }
}
