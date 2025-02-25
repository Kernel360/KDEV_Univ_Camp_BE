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

    // ✅ CORS 설정 (외부 API 및 폰트 허용)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ✅ 모든 도메인 허용
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));

        // ✅ 모든 HTTP 메서드 허용
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ 모든 헤더 허용
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // ✅ Authorization 헤더 허용

        // ✅ 자격 증명 (Credentials) 허용
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
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ CORS 적용
                .csrf(csrf -> csrf.disable()) // ✅ CSRF 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicMatchers).permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; " +
                                        "upgrade-insecure-requests; " +  // ✅ HTTP → HTTPS 자동 업그레이드
                                        "frame-ancestors 'self'; " +  // ✅ 프레임 허용
                                        "connect-src 'self' " +
                                        "http://ec2-52-79-227-43.ap-northeast-2.compute.amazonaws.com:8080 " +
                                        "https://ec2-52-79-227-43.ap-northeast-2.compute.amazonaws.com:8443 " +
                                        "wss://ec2-52-79-227-43.ap-northeast-2.compute.amazonaws.com:8443 " +
                                        "https://fonts.googleapis.com; " +  // ✅ Google Fonts API 요청 허용
                                        "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                                        "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +  // ✅ Google Fonts 스타일 허용
                                        "img-src 'self' data: https://*; " +
                                        "font-src 'self' https://fonts.gstatic.com https://fonts.googleapis.com https://fonts.gstatic.com/ea/notosanskr/v2; "))) // ✅ 폰트 허용

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // ✅ JWT 필터 추가
                .build();
    }
}
