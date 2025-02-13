package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Tag(name = "차량 운행 상태", description = "실시간 차량 운행 및 배터리 상태 정보를 제공합니다.")
@RestController
@RequestMapping("/api/vehicle-status")
public class VehicleStatusController {

    @Operation(
            summary = "차량 운행 상태 조회",
            description = "운행 중, 미운행, 미관제 차량의 개수를 포함하여 전체 차량 개수를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "운행 상태 정보 응답",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                        {
                            "totalVehicles": 50,
                            "unmonitoredVehicles": 10,
                            "nonOperatingVehicles": 15,
                            "operatingVehicles": 25
                        }
                    """)
                            )
                    )
            }
    )
    @GetMapping
    public Map<String, Integer> getVehicleStatus() {
        int operating = 25;    // 운행 중 차량 수
        int nonOperating = 15; // 미운행 차량 수
        int unmonitored = 10;  // 미관제 차량 수
        int total = operating + nonOperating + unmonitored; // 전체 차량 수

        return Map.of(
                "totalVehicles", total,
                "unmonitoredVehicles", unmonitored,
                "nonOperatingVehicles", nonOperating,
                "operatingVehicles", operating
        );
    }

    @Operation(
            summary = "차량 배터리 상태 조회",
            description = "차량 번호와 함께 배터리 상태(0~100%)를 제공합니다. 30% 이하이면 경고가 필요합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "배터리 상태 정보 응답",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                        [
                            {"vehicleNumber": "12가1234", "batteryLevel": 85},
                            {"vehicleNumber": "34나5678", "batteryLevel": 20}
                        ]
                    """)
                            )
                    )
            }
    )
    @GetMapping("/battery")
    public List<Map<String, Object>> getVehicleBatteryStatus() {
        return List.of(
                Map.of("vehicleNumber", "12가1234", "batteryLevel", 85),
                Map.of("vehicleNumber", "12가1234", "batteryLevel", 31),
                Map.of("vehicleNumber", "12가1234", "batteryLevel", 30),
                Map.of("vehicleNumber", "12가1234", "batteryLevel", 29)
        );
    }
}
