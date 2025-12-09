package me.silvernine.tutorial.service;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.SettingRequestDto;
import me.silvernine.tutorial.dto.SettingResponseDto;
import me.silvernine.tutorial.util.ResponseCode;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingService {

    public SettingResponseDto processSetting(SettingRequestDto request) {
        if (request.getMdn() == null || request.getOTime() == null) {
            return SettingResponseDto.builder()
                    .rstCd(ResponseCode.MISSING_PARAMETER)
                    .rstMsg("Required parameter missing")
                    .build();
        }

        return SettingResponseDto.builder()
                .rstCd(ResponseCode.SUCCESS)
                .rstMsg("Success")
                .mdn(request.getMdn())
                .build();
    }
}
