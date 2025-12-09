package me.silvernine.tutorial.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SettingResponseDto {
    private String rstCd;
    private String rstMsg;
    private String mdn;
}
