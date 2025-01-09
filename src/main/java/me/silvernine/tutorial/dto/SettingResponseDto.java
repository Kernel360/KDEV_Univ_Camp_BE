package me.silvernine.tutorial.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SettingResponseDto {
    private String rstCd;  // 결과 코드
    private String rstMsg; // 결과 메시지
    private String mdn;    // 차량 번호
}