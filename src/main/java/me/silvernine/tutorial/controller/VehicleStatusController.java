package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Tag(name = "차량 운행 상태", description = "실시간 차량 운행 및 대여 정보를 제공합니다.")
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
            summary = "차량 개별 상태 및 대여 정보 조회",
            description = "차량 번호를 입력하면 해당 차량의 최신 배터리 상태, 운행 상태, 출발/도착 날짜 및 대여/반납 시간을 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "차량 상태 및 대여 정보 응답",
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
                            "endDate": "2025-03-24"
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
        // ✅ 차량 정보를 가져옴 (더미 데이터)
        Map<String, Object> vehicleData = getDummyVehicleData(vehicleNumber);

        if (vehicleData == null) {
            throw new RuntimeException("해당 차량 번호를 찾을 수 없습니다: " + vehicleNumber);
        }

        return vehicleData;
    }

    /**
     * ✅ 더미 데이터: 차량의 배터리 상태, 운행 상태, 출발/도착 날짜 및 대여/반납 시간을 반환
     */
    private Map<String, Object> getDummyVehicleData(String vehicleNumber) {
        return switch (vehicleNumber) {
            case "12가1234" -> Map.of(
                    "vehicleNumber", "12가1234",
                    "batteryLevel", 85,
                    "status", "운행 중",
                    "rentalLocation", "서울",
                    "rentalDateTime", formatTime("2025-01-01T09:00:00"),
                    "returnLocation", "부산",
                    "returnDateTime", formatTime("2025-03-24T23:00:00"),
                    "startDate", formatDate("2025-01-01T09:00:00"),
                    "endDate", formatDate("2025-03-24T23:00:00")
            );
            case "34나5678" -> Map.of(
                    "vehicleNumber", "34나5678",
                    "batteryLevel", 20,
                    "status", "미운행",
                    "rentalLocation", "대전",
                    "rentalDateTime", formatTime("2025-02-15T10:30:00"),
                    "returnLocation", "광주",
                    "returnDateTime", formatTime("2025-03-20T18:45:00"),
                    "startDate", formatDate("2025-02-15T10:30:00"),
                    "endDate", formatDate("2025-03-20T18:45:00")
            );
            case "78다9012" -> Map.of(
                    "vehicleNumber", "78다9012",
                    "batteryLevel", 45,
                    "status", "미관제",
                    "rentalLocation", "인천",
                    "rentalDateTime", formatTime("2025-03-10T14:15:00"),
                    "returnLocation", "울산",
                    "returnDateTime", formatTime("2025-03-25T22:10:00"),
                    "startDate", formatDate("2025-03-10T14:15:00"),
                    "endDate", formatDate("2025-03-25T22:10:00")
            );
            default -> null;
        };
    }

    /**
     * ✅ "yyyy-MM-dd HH:mm:ss" 형식의 날짜 데이터를 "HH:mm" (시:분) 형식으로 변환
     */
    private String formatTime(String dateTime) {
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return parsedDateTime.format(formatter);
    }

    /**
     * ✅ "yyyy-MM-dd HH:mm:ss" 형식의 날짜 데이터를 "yyyy-MM-dd" (년-월-일) 형식으로 변환
     */
    private String formatDate(String dateTime) {
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return parsedDateTime.format(formatter);
    }
}
