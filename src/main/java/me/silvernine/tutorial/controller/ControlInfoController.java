package me.silvernine.tutorial.controller;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.ControlInfoRequestDto;
import me.silvernine.tutorial.dto.ControlInfoResponseDto;
import me.silvernine.tutorial.service.ControlInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Control Info Management", description = "APIs for managing control information")
@RestController
@RequestMapping("/api/control")
@RequiredArgsConstructor
public class ControlInfoController {

    private final ControlInfoService controlInfoService;

    @Operation(
            summary = "Get and set control info",
            description = "Processes a control information request and returns the updated information"
    )
    @PostMapping("/getSetInfo")
    public ResponseEntity<ControlInfoResponseDto> getSetInfo(@RequestBody ControlInfoRequestDto request) {
        ControlInfoResponseDto response = controlInfoService.getControlInfo(request);
        return ResponseEntity.ok(response);
    }
}
