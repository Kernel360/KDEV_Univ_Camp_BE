package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

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
            summary = "차량 배터리 상태 조회",
            description = "차량 번호를 입력하면 해당 차량의 최신 배터리 상태를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "배터리 상태 정보 응답",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                        {
                            "vehicleNumber": "12가1234",
                            "batteryLevel": 85
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
    @GetMapping("/battery/{vehicleNumber}")
    public Map<String, Object> getVehicleBatteryStatus(@PathVariable String vehicleNumber) {
        // ✅ 현재는 더미 데이터를 반환하지만, 이후 DB에서 조회하도록 변경 가능
        if (!"12가1234".equals(vehicleNumber)) {
            throw new RuntimeException("해당 차량 번호를 찾을 수 없습니다: " + vehicleNumber);
        }

        return Map.of(
                "vehicleNumber", vehicleNumber,
                "batteryLevel", 85 // 더미 데이터, 이후 DB에서 가져오도록 변경 가능
        );
    }
}
