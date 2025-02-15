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
                            "rentalLocation": "서울",
                            "rentalDateTime": "2025-01-01T09:00:00",
                            "returnLocation": "부산",
                            "returnDateTime": "2025-03-24T23:00:00",
                            "startDate": "2025-01-01",
                            "endDate": "2025-03-24",
                            "totalDrivingHours": "283시간",
                            "dailyDrivingHours": "4시간 30분"
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

        // 기본 거리값 설정 (이미지의 데이터와 유사하게)
        int[] thisWeekDistances = {120, 200, 150, 80, 70, 110, 130};
        int[] lastWeekDistances = {90, 150, 120, 60, 50, 90, 100};

        String[] daysOfWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

        // 현재 요일 가져오기 (1: 월요일, 7: 일요일)
        LocalDate today = LocalDate.now();
        int currentDayOfWeek = today.getDayOfWeek().getValue();

        // 총 주행거리 계산 (현재 요일까지만)
        double totalDistance = 0;
        for (int i = 0; i < currentDayOfWeek; i++) {
            totalDistance += thisWeekDistances[i];
        }

        // 요일별 데이터 생성 (현재 요일까지만)
        for (int i = 0; i < currentDayOfWeek; i++) {
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("dayOfWeek", daysOfWeek[i]);

            // 이번 주 데이터에 ±10km 랜덤값 추가
            int thisWeekDistance = thisWeekDistances[i] + random.nextInt(21) - 10;
            dayData.put("thisWeek", thisWeekDistance);

            // 지난 주 데이터에 ±10km 랜덤값 추가
            int lastWeekDistance = lastWeekDistances[i] + random.nextInt(21) - 10;
            dayData.put("lastWeek", lastWeekDistance);

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

        // 날짜 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime rentalDate = LocalDateTime.parse(rentalDateTime, formatter);
        LocalDateTime returnDate = LocalDateTime.parse(returnDateTime, formatter);

        // 전체 대여 기간 (일) 계산
        long totalDays = ChronoUnit.DAYS.between(rentalDate, returnDate);

        // 평균 일일 운행시간을 3~6시간으로 가정하고 랜덤값 생성
        int avgDailyHours = random.nextInt(4) + 3;

        // 전체 운행 시간 계산 (일일 운행시간 * 대여 일수)
        long totalDrivingHours = totalDays * avgDailyHours;

        // 당일 운행 시간 계산 (0~avgDailyHours 시간 + 0~59분)
        int dailyHours = random.nextInt(avgDailyHours + 1);
        int dailyMinutes = random.nextInt(60);

        // 시간대별 주행거리 데이터 생성
        List<Map<String, Object>> hourlyDistances = generateHourlyDistances();

        vehicleData.put("vehicleNumber", vehicleNumber);
        vehicleData.put("batteryLevel", batteryLevel);
        vehicleData.put("status", status);
        vehicleData.put("rentalLocation", rentalLocation);
        vehicleData.put("rentalDateTime", rentalDateTime);
        vehicleData.put("returnLocation", returnLocation);
        vehicleData.put("returnDateTime", returnDateTime);
        vehicleData.put("startDate", rentalDateTime.substring(0, 10));
        vehicleData.put("endDate", returnDateTime.substring(0, 10));
        vehicleData.put("totalDrivingHours", totalDrivingHours + "시간");
        vehicleData.put("dailyDrivingHours", String.format("%d시간 %d분", dailyHours, dailyMinutes));
        vehicleData.put("hourlyDistances", hourlyDistances);

        return vehicleData;
    }

    /**
     * ✅ 2시간 단위로 주행거리 데이터 생성
     */
    private List<Map<String, Object>> generateHourlyDistances() {
        List<Map<String, Object>> hourlyDistances = new ArrayList<>();
        String[] timeRanges = {
                "0-2", "2-4", "4-6", "6-8", "8-10", "10-12",
                "12-14", "14-16", "16-18", "18-20", "20-22", "22-24"
        };

        // 기본 거리값 설정 (이미지의 데이터와 유사하게)
        int[] baseDistances = {
                85, 132, 45, 167, 93, 223,
                156, 78, 189, 112, 145, 92
        };

        for (int i = 0; i < timeRanges.length; i++) {
            Map<String, Object> distanceData = new HashMap<>();
            distanceData.put("timeRange", timeRanges[i]);

            // 기본 거리에서 ±10km 정도의 랜덤값 추가
            int distance = baseDistances[i] + random.nextInt(21) - 10;
            distanceData.put("distance", distance);

            hourlyDistances.add(distanceData);
        }

        return hourlyDistances;
    }
}