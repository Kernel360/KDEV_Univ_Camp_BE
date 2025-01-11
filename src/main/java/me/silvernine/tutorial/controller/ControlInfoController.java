package me.silvernine.tutorial.controller;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.ControlInfoRequestDto;
import me.silvernine.tutorial.dto.ControlInfoResponseDto;
import me.silvernine.tutorial.service.ControlInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/control")
@RequiredArgsConstructor
public class ControlInfoController {

    private final ControlInfoService controlInfoService;

    @PostMapping("/getSetInfo")
    public ResponseEntity<ControlInfoResponseDto> getSetInfo(@RequestBody ControlInfoRequestDto request) {
        ControlInfoResponseDto response = controlInfoService.getControlInfo(request);
        return ResponseEntity.ok(response);
    }
}
