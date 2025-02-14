package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.silvernine.tutorial.dto.TripRequestDto;
import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Trip Controller", description = "🚗 차량 GPS 데이터를 관리하는 API입니다.")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/trip")
public class TripController {

    @Autowired
    private TripService tripService;

    private static int tripInterval = 60;

    @Operation(summary = "📌 GPS 저장 주기 설정", description = "자동 저장되는 GPS 데이터의 주기를 설정합니다. (60, 120, 180초)")
    @PostMapping("/setFrequency")
    public ResponseEntity<String> setFrequency(@RequestParam int interval) {
        if (interval == 60 || interval == 120 || interval == 180) {
            tripInterval = interval;
            return ResponseEntity.ok("주기 설정 완료: " + interval + "초");
        } else {
            return ResponseEntity.badRequest().body("잘못된 주기 값입니다. 60, 120, 180 중 선택하세요.");
        }
    }

    @Scheduled(fixedRateString = "#{T(java.lang.Integer).parseInt(@tripController.tripInterval) * 1000}")
    public void saveGpsDataScheduled() {
        tripService.saveGpsData();
        System.out.println("자동 저장 실행됨 (주기: " + tripInterval + "초)");
    }

    @Operation(summary = "📌 단일 GPS 데이터 저장", description = "🚗 하나의 GPS 데이터를 저장합니다.")
    @PostMapping
    public ResponseEntity<?> saveTrip(@RequestBody TripRequestDto tripDto) {
        // ✅ 요청 로깅
        System.out.println("✅ [API 요청 수신] " + tripDto);
        System.out.println("📌 vehicleId: " + tripDto.getVehicleId());
        System.out.println("📌 timestamp: " + tripDto.getTimestamp());
        System.out.println("📌 latitude: " + tripDto.getLatitude());
        System.out.println("📌 longitude: " + tripDto.getLongitude());
        System.out.println("📌 batteryLevel: " + tripDto.getBatteryLevel());

        try {
            Trip trip = new Trip();
            trip.setVehicleId(tripDto.getVehicleId());

            // ✅ Timestamp 변환 (밀리초까지 포함된 경우 자동 처리)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            LocalDateTime timestamp = LocalDateTime.parse(tripDto.getTimestamp(), formatter);
            trip.setTimestamp(timestamp);

            trip.setLatitude(tripDto.getLatitude());
            trip.setLongitude(tripDto.getLongitude());
            trip.setBatteryLevel(tripDto.getBatteryLevel());

            tripService.saveTrip(trip);
            return ResponseEntity.ok().body("{\"message\": \"Success\"}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"error\": \"Invalid timestamp format\"}");
        }
    }

    @Operation(summary = "📌 배치 GPS 데이터 저장", description = "🚗 여러 개의 GPS 데이터를 한 번에 저장합니다.")
    @PostMapping("/batch")
    @PreAuthorize("permitAll()")  // ✅ JWT 없이 호출 가능하도록 허용
    public ResponseEntity<?> saveTrips(@RequestBody List<TripRequestDto> tripRequestDtos) {
        System.out.println("📌 Received Trip Data: " + tripRequestDtos);

        if (tripRequestDtos.isEmpty()) {
            System.err.println("❌ 클라이언트에서 보낸 데이터가 비어 있음!");
            return ResponseEntity.badRequest().body("Received empty data");
        }

        List<Trip> trips = tripRequestDtos.stream().map(dto -> {
            Trip trip = new Trip();
            trip.setVehicleId(dto.getVehicleId());

            try {
                // ✅ 밀리초까지 포함한 포맷 적용
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

                // 🔥 밀리초가 없는 경우도 고려하여 변환
                String formattedTimestamp = dto.getTimestamp().replace(".00", ""); // .00 제거
                LocalDateTime timestamp = LocalDateTime.parse(formattedTimestamp, formatter);
                trip.setTimestamp(timestamp);
            } catch (Exception e) {
                System.err.println("🚨 Timestamp 변환 실패: " + dto.getTimestamp());
                return null;
            }

            trip.setLatitude(dto.getLatitude());
            trip.setLongitude(dto.getLongitude());
            trip.setBatteryLevel(dto.getBatteryLevel());

            System.out.println("✅ 변환된 Trip 데이터: " + trip);
            return trip;
        }).filter(trip -> trip != null).collect(Collectors.toList());

        if (trips.isEmpty()) {
            return ResponseEntity.badRequest().body("❌ 변환된 데이터가 없음!");
        }

        tripService.saveTrips(trips);
        return ResponseEntity.ok().body("✅ Data saved successfully");
    }

    @Operation(summary = "📌 모든 GPS 데이터 조회", description = "저장된 모든 GPS 데이터를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    @Operation(summary = "📌 주기별 GPS 데이터 조회", description = "설정된 주기(60, 120, 180초)마다 저장된 GPS 데이터를 조회합니다.")
    @GetMapping("/gpsData")
    public ResponseEntity<List<Trip>> getGpsData(@RequestParam int interval) {
        if (interval == 60 || interval == 120 || interval == 180) {
            List<Trip> trips = tripService.getTripsByInterval(interval);
            return ResponseEntity.ok(trips.isEmpty() ? List.of() : trips);
        } else {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

    @Operation(summary = "📌 특정 차량 최신 GPS 데이터 조회", description = "🚗 특정 차량의 가장 최근 GPS 데이터를 조회합니다.")
    @GetMapping("/latestGpsData")
    public ResponseEntity<List<Trip>> getLatestGpsData(@RequestParam String vehicleId) {
        List<Trip> latestGpsData = tripService.getLatestGpsDataByVehicle(vehicleId);

        if (latestGpsData.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }
        return ResponseEntity.ok(latestGpsData);
    }
}
