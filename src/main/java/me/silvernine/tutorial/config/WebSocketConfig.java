package me.silvernine.tutorial.config;

import me.silvernine.tutorial.handler.BatteryWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final BatteryWebSocketHandler batteryWebSocketHandler;

    public WebSocketConfig(BatteryWebSocketHandler batteryWebSocketHandler) {
        this.batteryWebSocketHandler = batteryWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(batteryWebSocketHandler, "/battery-status")
                .setAllowedOrigins("*"); // 보안 설정이 필요하면 특정 도메인으로 변경
    }
}
