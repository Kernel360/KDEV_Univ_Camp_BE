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
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtFilter jwtFilter;

    public SecurityConfig(TokenProvider tokenProvider, JwtFilter jwtFilter) {
        this.tokenProvider = tokenProvider;
        this.jwtFilter = jwtFilter;
    }

    // ✅ CORS 설정 (프론트엔드 URL 허용)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "https://kdev-univ-camp-fe.vercel.app",  // ✅ Vercel 프론트엔드 추가
                "https://ec2-52-79-227-43.ap-northeast-2.compute.amazonaws.com"
        ));
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // ✅ CSRF 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()  // ✅ Swagger UI 허용
                        .requestMatchers("/api/**").permitAll()  // ✅ 모든 API 요청 허용
                        .anyRequest().authenticated())  // ✅ 나머지는 인증 필요
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("default-src 'self'; " +
                                        "connect-src 'self' " +
                                        "https://ec2-52-79-227-43.ap-northeast-2.compute.amazonaws.com:8443 " +  // ✅ 백엔드 API 허용
                                        "https://kdev-univ-camp-fe.vercel.app; " +  // ✅ Vercel 프론트엔드 허용
                                        "script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +
                                        "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; " +
                                        "img-src 'self' data: https://*; " +
                                        "font-src 'self' data: https://fonts.gstatic.com https://fonts.googleapis.com; ")))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
