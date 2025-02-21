package me.silvernine.tutorial.service;

import me.silvernine.tutorial.handler.BatteryWebSocketHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BatteryMonitoringService {
    private final BatteryWebSocketHandler batteryWebSocketHandler;
    private int batteryLevel = 100; // 초기 배터리 상태
    private boolean charging = false; // 🔋 충전 상태 (true: 충전 중, false: 방전 중)

    public BatteryMonitoringService(BatteryWebSocketHandler batteryWebSocketHandler) {
        this.batteryWebSocketHandler = batteryWebSocketHandler;
    }

    @Scheduled(fixedRate = 30000) // ⏳ 30초마다 실행
    public void monitorBattery() {
        int previousLevel = batteryLevel;

        // 🔋 배터리가 20% 이하가 되면 자동 충전 시작
        if (batteryLevel <= 20 && !charging) {
            charging = true; // 충전 상태 ON
            System.out.println("🔌 배터리 부족! 충전 시작!");
        }

        // 🔌 충전 중이면 1%씩 증가
        if (charging) {
            batteryLevel = Math.min(100, batteryLevel + 1);
            if (batteryLevel == 100) {
                charging = false; // 배터리가 100%가 되면 충전 중지
                System.out.println("🔋 배터리 완충 완료! 충전 중지");
            }
        } else {
            // 🔋 방전 중이면 1%씩 감소
            batteryLevel = Math.max(0, batteryLevel - 1);
        }

        // 🔄 상태가 변경된 경우 WebSocket 전송
        if (previousLevel != batteryLevel) {
            sendBatteryStatus();
        }
    }

    // 🔄 WebSocket으로 상태 전송
    private void sendBatteryStatus() {
        String message = "{\"battery\": " + batteryLevel + ", \"charging\": " + charging + "}";

        batteryWebSocketHandler.sendBatteryStatus(batteryLevel, charging);
        System.out.println("🔋 배터리 상태 변경: " + batteryLevel + "%, 충전 중: " + charging);
    }
}
