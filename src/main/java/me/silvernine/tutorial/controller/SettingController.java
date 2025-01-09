package me.silvernine.tutorial.controller;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.SettingRequestDto;
import me.silvernine.tutorial.dto.SettingResponseDto;
import me.silvernine.tutorial.service.SettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    @PostMapping("/sendSetting")
    public ResponseEntity<SettingResponseDto> sendSetting(@RequestBody SettingRequestDto request) {
        SettingResponseDto response = settingService.processSetting(request);
        return ResponseEntity.ok(response);
    }
}
