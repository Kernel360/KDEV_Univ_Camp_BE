package me.silvernine.tutorial.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String NICKNAME_KEY = "nickname"; // ✅ 닉네임 저장을 위한 키
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final Key key;
    private final long tokenValidityInMilliseconds;

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        System.out.println("✅ [JWT 초기화] Secret Key 설정 완료, 유효시간(ms): " + tokenValidityInMilliseconds);
    }

    public String createToken(Authentication authentication, String nickname) {
        System.out.println("🚀 [JWT 생성 시작] Authentication Name: " + authentication.getName());

        if (nickname == null || nickname.isEmpty()) {
            System.out.println("❌ 닉네임이 존재하지 않음. 기본값 'DefaultUser' 설정");
            nickname = "DefaultUser";
        }

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        System.out.println("✅ [JWT 생성] 권한 목록: " + authorities);

        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenValidityInMilliseconds);

        try {
            String jwt = Jwts.builder()
                    .setSubject(authentication.getName())
                    .claim(AUTHORITIES_KEY, authorities)
                    .claim(NICKNAME_KEY, nickname)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .setExpiration(validity)
                    .compact();

            System.out.println("🔑 생성된 JWT: " + jwt);
            return jwt;
        } catch (Exception e) {
            System.out.println("❌ JWT 생성 실패: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public String createToken(String userId, Collection<? extends GrantedAuthority> authorities) {
        System.out.println("🚀 [JWT 생성 시작] userId: " + userId);

        String authString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        System.out.println("✅ [JWT 생성] 권한 목록: " + authString);

        long now = (new Date()).getTime();
        Date validity = new Date(now + tokenValidityInMilliseconds);

        try {
            String jwt = Jwts.builder()
                    .setSubject(userId)
                    .claim(AUTHORITIES_KEY, authString)
                    .signWith(key, SignatureAlgorithm.HS512)
                    .setExpiration(validity)
                    .compact();

            System.out.println("🔑 생성된 JWT: " + jwt);
            return jwt;
        } catch (Exception e) {
            System.out.println("❌ JWT 생성 실패: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Authentication getAuthentication(String token) {
        System.out.println("🚀 [JWT 검증] 토큰 값: " + token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String authoritiesString = claims.get(AUTHORITIES_KEY).toString();
        System.out.println("✅ [JWT 검증] 권한 값: " + authoritiesString);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(authoritiesString.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        System.out.println("✅ [JWT 검증 완료] 사용자 ID: " + claims.getSubject());

        return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        System.out.println("🚀 [JWT 유효성 검사 시작] 토큰: " + token);

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            System.out.println("✅ [JWT 유효성 검사 통과]");
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT 토큰이 잘못되었습니다.");
        }

        System.out.println("❌ [JWT 유효성 검사 실패]");
        return false;
    }
}
