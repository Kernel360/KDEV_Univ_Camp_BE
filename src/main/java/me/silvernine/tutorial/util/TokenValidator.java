package me.silvernine.tutorial.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class TokenValidator {

    private static final Logger logger = LoggerFactory.getLogger(TokenValidator.class);
    private final SecretKey secretKey;

    // ✅ Base64 디코딩을 추가하여 `TokenProvider`와 동일한 방식으로 처리
    public TokenValidator(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 토큰의 유효성을 검사하는 메서드
     */
    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            logger.info("✅ JWT 검증 성공: {}", token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("❌ JWT 만료됨: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("❌ JWT 형식 오류: {}", e.getMessage());
        } catch (JwtException e) {
            logger.error("❌ JWT 검증 오류: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("❌ 기타 JWT 검증 오류: {}", e.getMessage());
        }
        return false;
    }

    /**
     * JWT 토큰에서 Claims(클레임) 추출
     */
    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.error("❌ JWT 만료됨: {}", e.getMessage());
            return e.getClaims(); // 만료된 경우에도 클레임 반환 가능
        } catch (JwtException e) {
            logger.error("❌ JWT 클레임 추출 오류: {}", e.getMessage());
            return null;
        }
    }
}
