package me.silvernine.tutorial.controller;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.SettingRequestDto;
import me.silvernine.tutorial.dto.SettingResponseDto;
import me.silvernine.tutorial.service.SettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Settings Management", description = "APIs for managing system settings")
@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    @Operation(
            summary = "Send and process settings",
            description = "Receives setting data from the client, processes it, and returns the result"
    )
    @PostMapping("/sendSetting")
    public ResponseEntity<SettingResponseDto> sendSetting(@RequestBody SettingRequestDto request) {
        SettingResponseDto response = settingService.processSetting(request);
        return ResponseEntity.ok(response);
    }
}
