package me.silvernine.tutorial.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider {
    private final Key key;
    private final long tokenValidityInMilliseconds = 3600000; // 1시간

    public TokenProvider() {
        byte[] keyBytes = Decoders.BASE64.decode("your-secret-key");
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createToken(Authentication authentication, String nickname) {
        if (nickname == null || nickname.isEmpty()) {
            throw new IllegalArgumentException("닉네임이 존재하지 않습니다.");
        }

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("nickname", nickname)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidityInMilliseconds))
                .compact();
    }
}
