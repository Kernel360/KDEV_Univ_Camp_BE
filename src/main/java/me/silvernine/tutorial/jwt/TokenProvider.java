package me.silvernine.tutorial.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenProvider {
    private final String secretKey = "your_secret_key"; // JWT 서명 키

    public String createToken(Authentication authentication, String nickname) {
        System.out.println("🚀 [JWT 생성 요청] 사용자=" + authentication.getName() + ", 닉네임=" + nickname);

        try {
            String jwt = Jwts.builder()
                    .setSubject(authentication.getName())
                    .claim("nickname", nickname)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1일 후 만료
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .compact();

            System.out.println("✅ JWT 생성 완료: " + jwt);
            return jwt;
        } catch (Exception e) {
            System.out.println("❌ JWT 생성 실패: " + e.getMessage());
            return null;
        }
    }
}
