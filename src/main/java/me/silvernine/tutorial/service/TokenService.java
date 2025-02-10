package me.silvernine.tutorial.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.TokenRequestDto;
import me.silvernine.tutorial.dto.TokenResponseDto;
import me.silvernine.tutorial.util.ResponseCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    // application.yml에서 jwt.secret 값을 가져옴
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // 토큰 만료 시간을 설정 (초 단위로 설정)
    @Value("${jwt.token-validity-in-seconds}")
    private long tokenValidityInSeconds;

    public TokenResponseDto generateToken(TokenRequestDto request) {
        // 입력값 검증
        if (request.getMdn() == null || request.getTid() == null) {
            return TokenResponseDto.builder()
                    .rstCd(ResponseCode.MISSING_PARAMETER)
                    .rstMsg("Required parameter missing")
                    .build();
        }

        // 토큰 만료 시간 계산
        long expirationTimeMillis = System.currentTimeMillis() + (tokenValidityInSeconds * 1000);

        // JWT 생성 로직
        String token = Jwts.builder()
                .setSubject(request.getMdn())  // 사용자 식별 정보 (MDN)
                .claim("tid", request.getTid()) // TID 추가
                .setIssuedAt(new Date()) // 발급 시간
                .setExpiration(new Date(expirationTimeMillis)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY) // 🔥 서명 적용 (HS512 알고리즘 사용)
                .compact();

        return TokenResponseDto.builder()
                .rstCd(ResponseCode.SUCCESS)
                .rstMsg("Success")
                .mdn(request.getMdn())
                .token(token)
                .exPeriod(String.valueOf(tokenValidityInSeconds / 3600)) // 시간을 기준으로 변환
                .build();
    }
}
