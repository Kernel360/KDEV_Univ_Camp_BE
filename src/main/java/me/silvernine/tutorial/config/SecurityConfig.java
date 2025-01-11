package me.silvernine.tutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        // H2 Console용 AntPathRequestMatcher
        AntPathRequestMatcher h2ConsoleRequestMatcher = new AntPathRequestMatcher("/h2-console/**");

        // Swagger용 AntPathRequestMatcher들
        AntPathRequestMatcher[] swaggerMatchers = {
                new AntPathRequestMatcher("/swagger-ui/**"),
                new AntPathRequestMatcher("/swagger-ui.html"),
                new AntPathRequestMatcher("/swagger-resources/**"),
                new AntPathRequestMatcher("/v3/api-docs/**"),
                new AntPathRequestMatcher("/webjars/**")
        };

        // API 엔드포인트용 MvcRequestMatcher
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector).servletPath("/");

        return http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(h2ConsoleRequestMatcher)
                        .ignoringRequestMatchers(swaggerMatchers)
                        .disable()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(h2ConsoleRequestMatcher).permitAll()
                        .requestMatchers(swaggerMatchers).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/hello")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/authenticate")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/signup")).permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )
                .build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}