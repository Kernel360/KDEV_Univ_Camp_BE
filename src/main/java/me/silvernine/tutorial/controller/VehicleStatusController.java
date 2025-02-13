package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;



@Tag(name = "차량 운행 상태", description = "실시간 차량 운행 및 대여 정보를 제공합니다.") // 🚀 오타 확인 후 수정
@RestController
@RequestMapping("/api/vehicle-status")
public class VehicleStatusController {

    @Operation(
            summary = "차량 개별 상태 및 운행 시간 조회",
            description = "차량 번호를 입력하면 해당 차량의 최신 배터리 상태, 운행 상태, 출발/도착 날짜 및 운행 누적 시간을 반환합니다.",
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
    @GetMapping("/{vehicleNumber}")
    public Map<String, Object> getVehicleDetails(@PathVariable String vehicleNumber) {
        Map<String, Object> vehicleData = getDummyVehicleData(vehicleNumber);
        if (vehicleData == null) {
            throw new RuntimeException("해당 차량 번호를 찾을 수 없습니다: " + vehicleNumber);
        }
        return vehicleData;
    }

    /**
     * ✅ 더미 데이터: 차량의 배터리 상태, 운행 상태, 출발/도착 날짜 및 운행 누적 시간 반환
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
     * ✅ 차량 데이터를 생성하는 메서드 (운행 시간 자동 계산)
     */
    private Map<String, Object> generateVehicleData(String vehicleNumber, int batteryLevel, String status,
                                                    String rentalLocation, String rentalDateTime,
                                                    String returnLocation, String returnDateTime) {
        // 날짜 변환
        LocalDate rentalDate = LocalDate.parse(rentalDateTime.substring(0, 10));
        LocalDate returnDate = LocalDate.parse(returnDateTime.substring(0, 10));

        // ✅ 대여일 수 계산 (최소 1일 보장)
        long rentalDays = Duration.between(rentalDate.atStartOfDay(), returnDate.atStartOfDay()).toDays() + 1;

        // ✅ 하루 5시간 운행으로 전체 운행 시간 계산
        long totalDrivingHours = rentalDays * 5;

        // ✅ Map.of 대신 HashMap 사용 (제한 없음)
        Map<String, Object> vehicleData = new HashMap<>();
        vehicleData.put("vehicleNumber", vehicleNumber);
        vehicleData.put("batteryLevel", batteryLevel);
        vehicleData.put("status", status);
        vehicleData.put("rentalLocation", rentalLocation);
        vehicleData.put("rentalDateTime", formatTime(rentalDateTime));
        vehicleData.put("returnLocation", returnLocation);
        vehicleData.put("returnDateTime", formatTime(returnDateTime));
        vehicleData.put("startDate", rentalDate.toString());
        vehicleData.put("endDate", returnDate.toString());
        vehicleData.put("totalDrivingHours", totalDrivingHours + "시간");  // 전체 운행 시간
        vehicleData.put("dailyDrivingHours", "5시간");  // 하루 운행 시간 고정

        return vehicleData;
    }

    /**
     * ✅ "yyyy-MM-dd HH:mm:ss" 형식의 날짜 데이터를 "HH:mm" (시:분) 형식으로 변환
     */
    private String formatTime(String dateTime) {
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return parsedDateTime.format(formatter);
    }
}
