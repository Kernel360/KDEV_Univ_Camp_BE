package me.silvernine.tutorial.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component // ✅ Spring 빈으로 등록
public class TokenValidator {

    private static final Logger logger = LoggerFactory.getLogger(TokenValidator.class);

    private final SecretKey secretKey; // ✅ SecretKey 객체로 변경

    // ✅ 생성자에서 jwt.secret 값을 SecretKey로 변환하여 초기화
    public TokenValidator(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder() // ✅ 최신 방식 적용
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("❌ JWT 만료됨: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("❌ JWT 형식 오류: {}", e.getMessage());
        } catch (JwtException e) { // ✅ SignatureException이 포함된 JwtException 처리
            logger.error("❌ JWT 검증 오류: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("❌ 기타 JWT 검증 오류: {}", e.getMessage());
        }
        return false;
    }
}
