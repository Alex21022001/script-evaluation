package com.alexsitiy.script.evaluation.controller;

import com.alexsitiy.script.evaluation.dto.ErrorMessage;
import com.alexsitiy.script.evaluation.dto.JSScriptFullReadDto;
import com.alexsitiy.script.evaluation.dto.JSScriptReadDto;
import com.alexsitiy.script.evaluation.exception.CapacityViolationException;
import com.alexsitiy.script.evaluation.model.JSScriptFilter;
import com.alexsitiy.script.evaluation.model.JSScriptSort;
import com.alexsitiy.script.evaluation.service.JSScriptExecutionService;
import com.alexsitiy.script.evaluation.service.JSScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.ok(jsService.findAll(filter, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JSScriptFullReadDto> findById(@PathVariable Integer id) {
        return jsService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping("/evaluate")
    public ResponseEntity<JSScriptFullReadDto> evaluate(@RequestBody String jsCode) {
        return ResponseEntity
                .status(201)
                .body(jsScriptExecutionService.evaluate(jsCode));
    }

    @PostMapping("/stop/{id}")
    public ResponseEntity<?> stop(@PathVariable Integer id) {

        return jsScriptExecutionService.stopById(id) ?
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(CapacityViolationException.class)
    public ResponseEntity<ErrorMessage> handleCapacityViolationException(CapacityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorMessage(ex.getMessage()));
    }

//    @GetMapping("/image")
//    public void chunkedImage(HttpServletResponse response){
//        Resource image = new ClassPathResource("static/chunked.jpg");
//
//        response.setHeader("Content-Type", MediaType.IMAGE_JPEG_VALUE);
//        response.setHeader("Transfer-Encoding","chunked");
//
//        try (InputStream inputStream = image.getInputStream()) {
//            ServletOutputStream outputStream = response.getOutputStream();
//            byte[] buffer = new byte[4096];
//            int bytesRead;
//
//            while ((bytesRead = inputStream.read(buffer)) != -1){
//                Thread.sleep(100);
//                outputStream.write(buffer,0,bytesRead);
//                outputStream.flush();
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
