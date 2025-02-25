package me.silvernine.tutorial.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {
   @Bean
   public CorsFilter corsFilter() {
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      CorsConfiguration config = new CorsConfiguration();

      config.setAllowedOrigins(Arrays.asList(
              "https://kdev-univ-camp-fe.vercel.app",
              "http://localhost:5173",
              "http://ec2-52-79-227-43.ap-northeast-2.compute.amazonaws.com",
              "https://ec2-52-79-227-43.ap-northeast-2.compute.amazonaws.com"
      ));
      config.setAllowCredentials(true);
      config.addAllowedHeader("*");
      config.addAllowedMethod("*");
      source.registerCorsConfiguration("/**", config);

      return new CorsFilter(source);
   }
}
