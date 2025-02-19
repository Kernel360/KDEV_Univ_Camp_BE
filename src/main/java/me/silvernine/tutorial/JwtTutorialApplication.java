package me.silvernine.tutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JwtTutorialApplication {
    public static void main(String[] args) {
        SpringApplication.run(JwtTutorialApplication.class, args);
    }
}
