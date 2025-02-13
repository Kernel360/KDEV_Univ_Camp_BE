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
public class TokenService {

    private final SecretKey secretKey;
    private final long tokenValidityInSeconds;

    public TokenService(@Value("1eooEb0QH4IPR2wP0ATY1a/UXh8+ERKkogA1ZKm79zt7UkJJH55GKLsyP5DkdOF8n+gXYJ3NpycJQ/D5UvwjSg==") String secret,
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
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        return TokenResponseDto.builder()
                .rstCd(ResponseCode.SUCCESS)
                .rstMsg("Token is valid")
                .token(token)
                .exPeriod(expirationTimeMillis) // ✅ 변수명 변경 (expPeriod → expirationTimeMillis)
                .build();
    }
}