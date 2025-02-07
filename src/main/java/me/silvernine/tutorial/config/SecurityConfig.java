package me.silvernine.tutorial.config;

import me.silvernine.tutorial.jwt.JwtFilter;
import me.silvernine.tutorial.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Autowired
    private TokenProvider tokenProvider;

    public SecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // ✅ PasswordEncoder를 Bean으로 등록 (필수)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // ✅ BCrypt 기반 암호화 적용
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AntPathRequestMatcher[] publicMatchers = {
                new AntPathRequestMatcher("/swagger-ui/**"),
                new AntPathRequestMatcher("/swagger-ui.html"),
                new AntPathRequestMatcher("/swagger-resources/**"),
                new AntPathRequestMatcher("/v3/api-docs/**"),
                new AntPathRequestMatcher("/webjars/**"),
                new AntPathRequestMatcher("/h2-console/**"),
                new AntPathRequestMatcher("/api/signup"),
                new AntPathRequestMatcher("/api/authenticate"),
                new AntPathRequestMatcher("/api/auth-header-check"),
                new AntPathRequestMatcher("/api/user")
        };

        return http
                .csrf(csrf -> csrf.disable()) // ✅ CSRF 보호 비활성화 (JWT 사용 시 필요 없음)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // ✅ 세션을 사용하지 않는 Stateless 정책 설정
                )
                .authorizeHttpRequests(auth -> auth
                        // ✅ Swagger 및 공용 API는 인증 없이 접근 가능
                        .requestMatchers(publicMatchers).permitAll()
                        // ✅ 모든 요청에 대해 JWT 인증 요구
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()) // ✅ H2 콘솔 사용을 위해 동일 출처 허용
                )
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class) // ✅ JWT 필터 적용
                .build();
    }
}
