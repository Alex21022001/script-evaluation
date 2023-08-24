package com.alexsitiy.script.evaluation.controller;

import com.alexsitiy.script.evaluation.dto.JSScriptFullReadDto;
import com.alexsitiy.script.evaluation.dto.JSScriptReadDto;
import com.alexsitiy.script.evaluation.exception.CapacityViolationException;
import com.alexsitiy.script.evaluation.model.JSScriptFilter;
import com.alexsitiy.script.evaluation.model.JSScriptSort;
import com.alexsitiy.script.evaluation.service.JSScriptExecutionService;
import com.alexsitiy.script.evaluation.service.JSScriptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<CollectionModel<JSScriptReadDto>> findAll(JSScriptFilter filter, JSScriptSort sort) {

        List<JSScriptReadDto> scripts = jsService.findAll(filter, sort);
        scripts.forEach(jsScriptDto -> jsScriptDto
                .add(linkTo(methodOn(JSScriptRestController.class).findById(jsScriptDto.getId())).withSelfRel()));

        CollectionModel<JSScriptReadDto> collectionModel = CollectionModel.of(scripts,
                linkTo(methodOn(JSScriptRestController.class).findAll(filter, sort)).withSelfRel());


        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JSScriptFullReadDto> findById(@PathVariable Integer id) {
        return jsService.findById(id)
                .map(jsScriptDto -> {
                    jsScriptDto
                            .add(linkTo(methodOn(JSScriptRestController.class).findById(id)).withSelfRel())
                            .add(linkTo(methodOn(JSScriptRestController.class).findAll(null, null)).withRel("allScripts").withType("GET"))
                            .add(linkTo(methodOn(JSScriptRestController.class).stop(jsScriptDto.getId())).withRel("stop").withType("POST").withDeprecation("Stop an executing Script"))
                            .add(linkTo(methodOn(JSScriptRestController.class).delete(jsScriptDto.getId())).withRel("delete").withType("DELETE").withDeprecation("Delete already executed Script"));
                    return ResponseEntity.ok(jsScriptDto);
                })
                .orElseGet(ResponseEntity.notFound()::build);
    }

    @PostMapping("/evaluate")
    public ResponseEntity<JSScriptFullReadDto> evaluate(@RequestBody String jsCode) {
        JSScriptFullReadDto jsScriptDto = jsScriptExecutionService.evaluate(jsCode);
        jsScriptDto
                .add(linkTo(methodOn(JSScriptRestController.class).evaluate(jsCode)).withSelfRel().withType("POST"))
                .add(linkTo(methodOn(JSScriptRestController.class).findById(jsScriptDto.getId())).withSelfRel().withType("GET").withDeprecation("Obtain JSScript data by its id"))
                .add(linkTo(methodOn(JSScriptRestController.class).findAll(null, null)).withRel("allScripts").withType("GET"))
                .add(linkTo(methodOn(JSScriptRestController.class).stop(jsScriptDto.getId())).withRel("stop").withType("POST").withDeprecation("Stop an executing Script"))
                .add(linkTo(methodOn(JSScriptRestController.class).delete(jsScriptDto.getId())).withRel("delete").withType("DELETE").withDeprecation("Delete already executed Script"));

        return ResponseEntity
                .status(201)
                .body(jsScriptDto);
    }

    @PostMapping("/stop/{id}")
    public ResponseEntity<?> stop(@PathVariable Integer id) {

        return jsScriptExecutionService.stopById(id) ?
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        return jsService.deleteExecutedTask(id) ?
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(CapacityViolationException.class)
    public ResponseEntity<Problem> handleCapacityViolationException(CapacityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Problem.builder()
                        .withStatus(Status.SERVICE_UNAVAILABLE)
                        .withDetail(ex.getMessage())
                        .build()
                );
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
