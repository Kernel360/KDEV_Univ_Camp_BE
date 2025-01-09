package me.silvernine.tutorial.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {
    private String rstCd; // 응답 코드
    private String rstMsg; // 응답 메시지
    private String mdn;    // 차량 번호
    private String token;  // 단말인증 토큰
    private String exPeriod; // 만료 기간
}