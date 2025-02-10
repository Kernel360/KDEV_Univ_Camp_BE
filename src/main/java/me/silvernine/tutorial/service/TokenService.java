package me.silvernine.tutorial.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.TokenRequestDto;
import me.silvernine.tutorial.dto.TokenResponseDto;
import me.silvernine.tutorial.util.ResponseCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final SecretKey secretKey;
    private final long tokenValidityInSeconds;

    // ✅ 생성자에서 SECRET_KEY를 SecretKey 객체로 변환
    public TokenService(@Value("${jwt.secret}") String secret,
                        @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.tokenValidityInSeconds = tokenValidityInSeconds;
    }

    public TokenResponseDto generateToken(TokenRequestDto request) {
        if (request.getMdn() == null || request.getTid() == null) {
            return TokenResponseDto.builder()
                    .rstCd(ResponseCode.MISSING_PARAMETER)
                    .rstMsg("Required parameter missing")
                    .build();
        }

        long expirationTimeMillis = System.currentTimeMillis() + (tokenValidityInSeconds * 1000);

        String token = Jwts.builder()
                .setSubject(request.getMdn())
                .claim("tid", request.getTid())
                .setIssuedAt(new Date())
                .setExpiration(new Date(expirationTimeMillis))
                .signWith(secretKey, SignatureAlgorithm.HS512)  // ✅ SecretKey 객체로 서명
                .compact();

        return TokenResponseDto.builder()
                .rstCd(ResponseCode.SUCCESS)
                .rstMsg("Success")
                .mdn(request.getMdn())
                .token(token)
                .exPeriod(String.valueOf(tokenValidityInSeconds / 3600))
                .build();
    }
}
