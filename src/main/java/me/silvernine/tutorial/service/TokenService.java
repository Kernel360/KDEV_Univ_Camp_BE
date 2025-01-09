package me.silvernine.tutorial.service;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.TokenRequestDto;
import me.silvernine.tutorial.dto.TokenResponseDto;
import me.silvernine.tutorial.util.ResponseCode;
import me.silvernine.tutorial.util.TokenGenerator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    public TokenResponseDto generateToken(TokenRequestDto request) {
        // 입력값 검증
        if (request.getMdn() == null || request.getTid() == null) {
            return TokenResponseDto.builder()
                    .rstCd(ResponseCode.MISSING_PARAMETER)
                    .rstMsg("Required parameter missing")
                    .build();
        }

        // 토큰 생성 로직
        String token = TokenGenerator.createToken(request.getMdn(), request.getTid());

        return TokenResponseDto.builder()
                .rstCd(ResponseCode.SUCCESS)
                .rstMsg("Success")
                .mdn(request.getMdn())
                .token(token)
                .exPeriod("4") // 고정 만료 기간 예시
                .build();
    }
}