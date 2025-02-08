package me.silvernine.tutorial.config;

import me.silvernine.tutorial.jwt.JwtFilter;
import me.silvernine.tutorial.jwt.TokenProvider;
import me.silvernine.tutorial.repository.UserRepository;
import me.silvernine.tutorial.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private TokenProvider tokenProvider;

    public SecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    // ✅ PasswordEncoder Bean 등록 (필수)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ UserDetailsService Bean 등록
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new CustomUserDetailsService(userRepository, passwordEncoder);
    }

    // ✅ AuthenticationManager 추가
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

    // ✅ CORS 설정 추가 (중복 제거 및 보완)
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(List.of("http://localhost:5173")); // 서브도메인까지 허용
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    // ✅ SecurityFilterChain 정의
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
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())  // ✅ CORS 필터를 따로 등록했으므로 중복 방지
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicMatchers).permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(corsFilter(), JwtFilter.class) // ✅ CORS 필터를 JWT 필터보다 먼저 실행
                .build();
    }
}
