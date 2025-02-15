package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Tag(name = "차량 상태 정보", description = "전체 차량 상태 및 특정 차량의 운행 정보를 제공합니다.")
@RestController
@RequestMapping("/api/vehicle-status")
public class VehicleStatusController {

    private final Random random = new Random();

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
                            "startLocation": "서울",
                            "startDate": "2025-01-01 09:00:00.00",
                            "returnLocation": "부산",
                            "returnDate": "2025-03-24 23:00:00.00",
                            "totalDrivingTime": 1018800000,
                            "dailyDrivingTime": 16200000
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

    @Operation(
            summary = "차량 주간 주행거리 조회",
            description = "차량의 이번 주와 지난 주의 요일별 주행거리를 반환합니다.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "주간 주행거리 응답",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = """
                    {
                        "totalDistance": 236.0,
                        "weeklyData": [
                            {
                                "dayOfWeek": "MON",
                                "thisWeek": 120,
                                "lastWeek": 90
                            },
                            {
                                "dayOfWeek": "TUE",
                                "thisWeek": 200,
                                "lastWeek": 150
                            }
                        ]
                    }
                """)
                            )
                    )
            }
    )
    @GetMapping("/weekly-distance/{vehicleNumber}")
    public Map<String, Object> getWeeklyDistance(@PathVariable String vehicleNumber) {
        Map<String, Object> weeklyData = generateWeeklyDistanceData();
        if (weeklyData == null) {
            throw new RuntimeException("해당 차량 번호를 찾을 수 없습니다: " + vehicleNumber);
        }
        return weeklyData;
    }

    /**
     * ✅ 주간 주행거리 데이터 생성
     */
    private Map<String, Object> generateWeeklyDistanceData() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> weeklyData = new ArrayList<>();

        // 이번 주 (월~토) 및 지난 주 (월~일) 거리값 설정
        int[] thisWeekDistances = {120, 200, 150, 80, 70, 110}; // 월~토
        int[] lastWeekDistances = {90, 150, 120, 60, 50, 90, 100}; // 월~일

        String[] daysOfWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

        // 총 주행거리 계산 (이번 주, 월~토)
        double totalDistance = 0;
        for (int distance : thisWeekDistances) {
            totalDistance += distance;
        }

        // 요일별 데이터 생성
        for (int i = 0; i < daysOfWeek.length; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("dayOfWeek", daysOfWeek[i]);

            // 이번 주 (월~토까지만) 및 지난 주 (월~일)
            dayData.put("thisWeek", i < thisWeekDistances.length ? thisWeekDistances[i] : null);
            dayData.put("lastWeek", i < lastWeekDistances.length ? lastWeekDistances[i] : null);

            weeklyData.add(dayData);
        }

        response.put("totalDistance", totalDistance);
        response.put("weeklyData", weeklyData);

        return response;
    }


    /**
     * ✅ 차량 개별 상태 및 운행 정보 조회용 더미 데이터
     */
    private Map<String, Object> getDummyVehicleData(String vehicleNumber) {
        return switch (vehicleNumber) {
            case "12가1234" -> generateVehicleData(
                    "12가1234", 85, "운행 중",
                    "서울", "2025-01-01 09:00:00.00",
                    "부산", "2025-03-24 23:00:00.00"
            );
            default -> null;
        };
    }

    /** ✅ 차량 데이터를 생성하는 메서드 (랜덤값 제거, 고정값 사용) */
    private Map<String, Object> generateVehicleData(String vehicleNumber, int batteryLevel, String status,
                                                    String rentalLocation, String rentalDateTime,
                                                    String returnLocation, String returnDateTime) {
        Map<String, Object> vehicleData = new HashMap<>();

        // 날짜 파싱
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");
        LocalDateTime rentalDate = LocalDateTime.parse(rentalDateTime, customFormatter);
        LocalDateTime returnDate = LocalDateTime.parse(returnDateTime, customFormatter);

        // 전체 대여 기간 (일) 계산
        long totalDays = ChronoUnit.DAYS.between(rentalDate, returnDate);

        // 평균 일일 운행시간 고정값 사용 (5시간)
        int avgDailyHours = 5;

        // 전체 운행 시간 계산 (일일 운행시간 * 대여 일수)
        long totalDrivingHours = totalDays * avgDailyHours;

        // 당일 운행 시간 고정값 사용 (4시간 30분)
        int dailyHours = 4;
        int dailyMinutes = 30;

        // 전체 운행 시간 (ms 단위)
        long totalDrivingTime = totalDays * avgDailyHours * 60L * 60 * 1000;

// 당일 운행 시간 (ms 단위)
        long dailyDrivingTime = (dailyHours * 60L * 60 * 1000) + (dailyMinutes * 60 * 1000);


        // 시간대별 주행거리 데이터 (고정값 사용)
        List<Map<String, Object>> hourlyDistances = generateHourlyDistances();

        vehicleData.put("vehicleNumber", vehicleNumber);
        vehicleData.put("batteryLevel", batteryLevel);
        vehicleData.put("status", status);
        vehicleData.put("rentalLocation", rentalLocation);
        vehicleData.put("returnLocation", returnLocation);
        vehicleData.put("startDate", rentalDateTime.substring(0, 10));
        vehicleData.put("endDate", returnDateTime.substring(0, 10));
        vehicleData.put("totalDrivingTime", totalDrivingTime); // ms 단위
        vehicleData.put("dailyDrivingTime", dailyDrivingTime);
        vehicleData.put("hourlyDistances", hourlyDistances);

        return vehicleData;
    }

    /**
     * ✅ 2시간 단위로 주행거리 데이터 생성
     */
    /** ✅ 2시간 단위로 주행거리 데이터 생성 (랜덤값 제거) */
    private List<Map<String, Object>> generateHourlyDistances() {
        List<Map<String, Object>> hourlyDistances = new ArrayList<>();
        String[] timeRanges = {
                "0-2", "2-4", "4-6", "6-8", "8-10", "10-12",
                "12-14", "14-16", "16-18", "18-20", "20-22", "22-24"
        };

        // 고정 거리값 설정 (랜덤 없이 고정값 반환)
        int[] baseDistances = {
                85, 132, 45, 167, 93, 223,
                156, 78, 189, 112, 145, 92
        };

        for (int i = 0; i < timeRanges.length; i++) {
            Map<String, Object> distanceData = new HashMap<>();
            distanceData.put("timeRange", timeRanges[i]);
            distanceData.put("distance", baseDistances[i]);
            hourlyDistances.add(distanceData);
        }

        return hourlyDistances;
    }
}