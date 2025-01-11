package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Example Controller", description = "Example endpoints for Swagger documentation")
@RestController
@RequestMapping("/api/example")
public class ExampleController {

    @Operation(summary = "Get example data", description = "Fetches example data from the server")
    @GetMapping("/data")
    public ResponseEntity<String> getExampleData() {
        return ResponseEntity.ok("Example Data");
    }
}
