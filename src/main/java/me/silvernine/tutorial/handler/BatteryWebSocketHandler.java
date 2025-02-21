package me.silvernine.tutorial.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class BatteryWebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public synchronized void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public synchronized void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
    }

    // ✅ 기존: 배터리 상태만 보내는 메서드 (유지)
    public synchronized void sendBatteryStatus(int batteryLevel) {
        String message = "{\"battery\": " + batteryLevel + "}";
        sendMessageToAll(message);
    }

    // ✅ 새로운 메서드: 배터리 상태 + 충전 여부까지 전송
    public synchronized void sendBatteryStatus(int batteryLevel, boolean charging) {
        String message = "{\"battery\": " + batteryLevel + ", \"charging\": " + charging + "}";
        sendMessageToAll(message);
    }

    // ✅ WebSocket 메시지를 모든 클라이언트에게 전송
    private void sendMessageToAll(String message) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
