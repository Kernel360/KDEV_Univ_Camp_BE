package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.CarResponse;
import me.silvernine.tutorial.entity.Car;
import me.silvernine.tutorial.service.CarService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "차량 상태 정보", description = "전체 차량 상태 및 특정 차량의 운행 정보를 제공합니다.")
@RestController
@RequestMapping("/api/vehicle-status")
@RequiredArgsConstructor
public class VehicleStatusController {

    private final CarService carService; // ✅ CarService 추가 (DB 조회용)

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
        CarResponse carResponse = carService.getCarByCarNumber(vehicleNumber); // ✅ Use CarResponse instead of Car

        Map<String, Object> vehicleData = new HashMap<>();
        vehicleData.put("vehicleNumber", carResponse.getCarNumber());
        vehicleData.put("carName", carResponse.getCarName());
        vehicleData.put("ownerUsername", carResponse.getOwnerUsername());

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

        // 기본 거리값 (예제용 고정값)
        Integer[] thisWeekDistances = {120, 200, 150, 80, 70, 110, null};
        Integer[] lastWeekDistances = {90, 150, 120, 60, 50, 90, 100};

        String[] daysOfWeek = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};

        // 총 주행거리 계산 (이번 주 합산)
        double totalDistance = 0;
        for (Integer distance : thisWeekDistances) {
            if (distance != null) {
                totalDistance += distance;
            }
        }

        // 배열 형태로 응답 구성
        response.put("daysOfWeek", daysOfWeek);
        response.put("thisWeekDistances", thisWeekDistances);
        response.put("lastWeekDistances", lastWeekDistances);
        response.put("totalDistance", totalDistance);

        return response;
    }
}
