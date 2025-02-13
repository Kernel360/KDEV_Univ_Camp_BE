package me.silvernine.tutorial.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {
    private String rstCd; // 응답 코드
    private String rstMsg; // 응답 메시지
    private String token;  // 단말인증 토큰
    private String username; // ✅ 사용자 ID 추가
    private long exPeriod;  // ✅ 만료 시간 타입을 long으로 변경
}
