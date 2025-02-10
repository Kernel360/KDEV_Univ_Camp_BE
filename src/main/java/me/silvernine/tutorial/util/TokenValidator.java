package me.silvernine.tutorial.util;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component // ✅ Spring 빈으로 등록
public class TokenValidator {

    @Value("${jwt.secret}") // ✅ application.yml에서 secret 값 가져오기
    private String secretKey;

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
            return false;
        }
    }
}
