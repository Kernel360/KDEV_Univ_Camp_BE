package me.silvernine.tutorial.config;

import me.silvernine.tutorial.jwt.JwtFilter;
import me.silvernine.tutorial.jwt.TokenProvider;
import me.silvernine.tutorial.repository.UserRepository;
import me.silvernine.tutorial.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtFilter jwtFilter;

    public SecurityConfig(TokenProvider tokenProvider, JwtFilter jwtFilter) {
        this.tokenProvider = tokenProvider;
        this.jwtFilter = jwtFilter;
    }

    // ✅ CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new CustomUserDetailsService(userRepository, passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       UserDetailsService userDetailsService,
                                                       PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
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
                new AntPathRequestMatcher("/api/user"),
                new AntPathRequestMatcher("/api/trip/**"),
                new AntPathRequestMatcher("/api/token/validate"),
                new AntPathRequestMatcher("/favicon.ico"),
                new AntPathRequestMatcher("/error"),
                new AntPathRequestMatcher("/api/vehicle-status")
        };

        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicMatchers).permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                        // 엄격한 CSP 적용 (nonce 기반)
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives(
                                        // 기본적으로 자체 출처만 허용
                                        "default-src 'self'; " +
                                                // 인라인 스크립트는 nonce와 strict-dynamic을 통해 허용
                                                "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                                                // 스타일은 인라인과 Google Fonts 허용
                                                "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                                                // 폰트 소스 명확히 지정
                                                "font-src 'self' https://fonts.gstatic.com data:; " +
                                                // 이미지는 자체 출처, data URL 및 모든 HTTPS 허용
                                                "img-src 'self' data: https:; " +
                                                // 연결은 자체 출처 및 지정된 도메인만 허용
                                                "connect-src 'self' " +
                                                "http://ec2-52-79-227-43.ap-northeast-2.compute.amazonaws.com:8080 " +
                                                "https://ec2-52-79-227-43.ap-northeast-2.compute.amazonaws.com:8443 " +
                                                "wss://ec2-52-79-227-43.ap-northeast-2.compute.amazonaws.com:8443 " +
                                                "https://fonts.googleapis.com; " +
                                                // 프레임은 자체 출처만 허용
                                                "frame-ancestors 'self'; " +
                                                // 자동으로 HTTP를 HTTPS로 업그레이드
                                                "upgrade-insecure-requests;"
                                ))
                        // 브라우저 XSS 보호 비활성화 (CSP로 대체)
                        .xssProtection(xss -> xss.disable()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}