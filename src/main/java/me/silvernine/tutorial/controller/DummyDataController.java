package me.silvernine.tutorial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dummy")
public class DummyDataController {

    @GetMapping("/vehicle-status")
    public Map<String, Integer> getDummyVehicleStatus() {
        int operating = 25;    // 운행 중 차량 수
        int notOperating = 15; // 미운행 차량 수
        int unmonitored = 10;  // 미관제 차량 수
        int total = operating + notOperating + unmonitored; // 전체 차량 수

        return Map.of(
                "운행 중 차량 수", operating,
                "미운행 차량 수", notOperating,
                "미관제 차량 수", unmonitored,
                "전체 차량 수", total
        );
    }
}
