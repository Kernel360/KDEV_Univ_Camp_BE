package me.silvernine.tutorial; // ✅ 올바른 패키지로 변경

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtTutorialApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwtTutorialApplication.class, args);
    }
}
