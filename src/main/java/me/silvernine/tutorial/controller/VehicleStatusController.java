package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "차량 상태 정보", description = "전체 차량 상태 및 특정 차량의 운행 정보를 제공합니다.")
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
            summary = "차량 운행 정보 조회",
            description = "차량 번호를 입력하면 해당 차량의 최신 배터리 상태 및 운행 정보를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "차량 상태 및 운행 정보 응답",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                        {
                            "vehicleNumber": "12가1234",
                            "batteryLevel": 85,
                            "status": "운행 중",
                            "rentalLocation": "서울",
                            "rentalDateTime": "09:00",
                            "returnLocation": "부산",
                            "returnDateTime": "23:00",
                            "startDate": "2025-01-01",
                            "endDate": "2025-03-24",
                            "totalDrivingHours": "395시간",
                            "dailyDrivingHours": "5시간"
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
    @GetMapping("/details/{vehicleNumber}")
    public Map<String, Object> getVehicleDetails(@PathVariable String vehicleNumber) {
        Map<String, Object> vehicleData = getDummyVehicleData(vehicleNumber);
        if (vehicleData == null) {
            throw new RuntimeException("해당 차량 번호를 찾을 수 없습니다: " + vehicleNumber);
        }
        return vehicleData;
    }

    /**
     * ✅ 차량 개별 상태 및 운행 정보 조회용 더미 데이터
     */
    private Map<String, Object> getDummyVehicleData(String vehicleNumber) {
        return switch (vehicleNumber) {
            case "12가1234" -> generateVehicleData(
                    "12가1234", 85, "운행 중",
                    "서울", "2025-01-01T09:00:00",
                    "부산", "2025-03-24T23:00:00"
            );
            case "34나5678" -> generateVehicleData(
                    "34나5678", 20, "미운행",
                    "대전", "2025-02-15T10:30:00",
                    "광주", "2025-03-20T18:45:00"
            );
            case "78다9012" -> generateVehicleData(
                    "78다9012", 45, "미관제",
                    "인천", "2025-03-10T14:15:00",
                    "울산", "2025-03-25T22:10:00"
            );
            default -> null;
        };
    }

    /**
     * ✅ 차량 데이터를 생성하는 메서드 (HashMap 사용)
     */
    private Map<String, Object> generateVehicleData(String vehicleNumber, int batteryLevel, String status,
                                                    String rentalLocation, String rentalDateTime,
                                                    String returnLocation, String returnDateTime) {
        Map<String, Object> vehicleData = new HashMap<>();
        vehicleData.put("vehicleNumber", vehicleNumber);
        vehicleData.put("batteryLevel", batteryLevel);
        vehicleData.put("status", status);
        vehicleData.put("rentalLocation", rentalLocation);
        vehicleData.put("rentalDateTime", rentalDateTime);
        vehicleData.put("returnLocation", returnLocation);
        vehicleData.put("returnDateTime", returnDateTime);
        vehicleData.put("startDate", rentalDateTime.substring(0, 10));
        vehicleData.put("endDate", returnDateTime.substring(0, 10));
        vehicleData.put("totalDrivingHours", "395시간");
        vehicleData.put("dailyDrivingHours", "5시간");

        return vehicleData;
    }
}
