package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.ControlInfoRequestDto;
import me.silvernine.tutorial.dto.ControlInfoResponseDto;
import me.silvernine.tutorial.service.ControlInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/control")
@Tag(name = "Control API", description = "API for managing control operations") // API 그룹 태그
@RequiredArgsConstructor
public class ControlInfoController {

    private final ControlInfoService controlInfoService;

    @PostMapping("/getSetInfo")
    @Operation(summary = "Get Control Info", description = "Fetches control and geo-fencing details for a vehicle") // 엔드포인트 설명
    public ResponseEntity<ControlInfoResponseDto> getSetInfo(@RequestBody ControlInfoRequestDto request) {
        ControlInfoResponseDto response = controlInfoService.getControlInfo(request);
        return ResponseEntity.ok(response);
    }
}
