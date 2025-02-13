package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "차량 상태 정보", description = "특정 차량의 실시간 운행 상태 및 배터리 정보를 제공합니다.")
@RestController
@RequestMapping("/api/vehicle-status")
public class VehicleStatusController {

    @Operation(
            summary = "전체 차량 운행 상태 조회",
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
        int operating = 25;
        int nonOperating = 15;
        int unmonitored = 10;
        int total = operating + nonOperating + unmonitored;

        return Map.of(
                "totalVehicles", total,
                "unmonitoredVehicles", unmonitored,
                "nonOperatingVehicles", nonOperating,
                "operatingVehicles", operating
        );
    }

    @Operation(
            summary = "차량 상태 조회",
            description = "차량 번호를 입력하면 해당 차량의 운행 상태 및 최신 배터리 상태를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "차량 상태 정보 응답",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                        {
                            "vehicleNumber": "12가1234",
                            "batteryLevel": 85,
                            "totalVehicles": 50,
                            "unmonitoredVehicles": 10,
                            "nonOperatingVehicles": 15,
                            "operatingVehicles": 25
                        }
                    """)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "차량을 찾을 수 없음"
                    )
            }
    )
    @GetMapping("/{vehicleNumber}")
    public Map<String, Object> getVehicleDetails(@PathVariable String vehicleNumber) {
        Map<String, Object> vehicleData = getDummyVehicleData(vehicleNumber);
        if (vehicleData == null) {
            throw new RuntimeException("해당 차량 번호를 찾을 수 없습니다: " + vehicleNumber);
        }
        return vehicleData;
    }

    /**
     * ✅ 더미 데이터: 특정 차량의 배터리 상태 및 차량 개수 정보 반환
     */
    private Map<String, Object> getDummyVehicleData(String vehicleNumber) {
        Map<String, Object> vehicleData = new HashMap<>();

        switch (vehicleNumber) {
            case "12가1234":
                vehicleData.put("vehicleNumber", "12가1234");
                vehicleData.put("batteryLevel", 85);
                break;
            case "34나5678":
                vehicleData.put("vehicleNumber", "34나5678");
                vehicleData.put("batteryLevel", 20);
                break;
            case "78다9012":
                vehicleData.put("vehicleNumber", "78다9012");
                vehicleData.put("batteryLevel", 45);
                break;
            default:
                return null;
        }

        // ✅ 기존 차량 상태 정보 추가 (전체 차량 개수 포함)
        vehicleData.put("totalVehicles", 50);
        vehicleData.put("unmonitoredVehicles", 10);
        vehicleData.put("nonOperatingVehicles", 15);
        vehicleData.put("operatingVehicles", 25);

        return vehicleData;
    }
}
