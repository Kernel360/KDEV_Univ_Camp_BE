package me.silvernine.tutorial.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfiguration {

    @Bean
    public SecretKey secretKey(@Value("1eooEb0QH4IPR2wP0ATY1a/UXh8+ERKkogA1ZKm79zt7UkJJH55GKLsyP5DkdOF8n+gXYJ3NpycJQ/D5UvwjSg==") String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}