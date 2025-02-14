package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.dto.TripRequestDto;
import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Trip Controller", description = "🚗 차량 GPS 데이터를 관리하는 API입니다.")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/trip")
public class TripController {

    private final TripService tripService;  // ✅ `@Autowired` 제거하고 `final` 유지

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
        System.out.println("✅ [API 요청 수신] " + tripDto);
        System.out.println("📌 type: " + tripDto.getType());
        System.out.println("📌 vehicleId: " + tripDto.getVehicleId());
        System.out.println("📌 date: " + tripDto.getDate());
        System.out.println("📌 time: " + tripDto.getTimestamp());  // JSON의 "time"이 여기에 매핑됨
        System.out.println("📌 latitude: " + tripDto.getLatitude());
        System.out.println("📌 longitude: " + tripDto.getLongitude());

        try {
            Trip trip = new Trip();
            trip.setVehicleId(tripDto.getVehicleId());

            // ✅ `date`가 비어 있지 않으면 "yyyy-MM-dd HH:mm:ss" 형식으로 조합
            String datePart = tripDto.getDate() == null || tripDto.getDate().isEmpty() ? "1970-01-01" : tripDto.getDate();
            String fixedTimestamp = (datePart + " " + tripDto.getTimestamp()).replace(".00", "").trim();
            DateTimeFormatter formatter;
            if (fixedTimestamp.contains(".")) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");  // 밀리초 포함
            } else {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  // 밀리초 없음
            }
            LocalDateTime parsedTimestamp = LocalDateTime.parse(fixedTimestamp, formatter);
            trip.setTimestamp(parsedTimestamp);

            trip.setLatitude(tripDto.getLatitude());
            trip.setLongitude(tripDto.getLongitude());

            // ✅ `battery_level` 기본값 처리
            trip.setBatteryLevel(tripDto.getBatteryLevel() != null ? tripDto.getBatteryLevel() : 100);

            tripService.saveTrip(trip);
            return ResponseEntity.ok().body("{\"message\": \"Success\"}");
        } catch (Exception e) {
            System.err.println("🚨 Timestamp 변환 실패: " + tripDto.getTimestamp());
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

        try {
            List<Trip> trips = tripRequestDtos.stream().map(dto -> {
                Trip trip = new Trip();
                trip.setVehicleId(dto.getVehicleId());

                try {
                    // ✅ `date`가 비어있으면 기본값을 "1970-01-01"로 설정
                    String datePart = dto.getDate() == null || dto.getDate().isEmpty() ? "1970-01-01" : dto.getDate();
                    String fixedTimestamp = (datePart + " " + dto.getTimestamp()).replace(".00", "").trim();
                    DateTimeFormatter formatter;
                    if (fixedTimestamp.contains(".")) {
                        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");  // 밀리초 포함
                    } else {
                        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  // 밀리초 없음
                    }
                    LocalDateTime timestamp = LocalDateTime.parse(fixedTimestamp, formatter);
                    trip.setTimestamp(timestamp);
                } catch (Exception e) {
                    throw new IllegalArgumentException("🚨 Timestamp 변환 실패: " + dto.getTimestamp());
                }

                trip.setLatitude(dto.getLatitude());
                trip.setLongitude(dto.getLongitude());

                // ✅ `battery_level`이 없는 경우 기본값 설정 (100)
                trip.setBatteryLevel(dto.getBatteryLevel() != null ? dto.getBatteryLevel() : 100);

                System.out.println("✅ 변환된 Trip 데이터: " + trip);
                return trip;
            }).collect(Collectors.toList());

            tripService.saveTrips(trips);
            return ResponseEntity.ok().body("✅ Data saved successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Operation(summary = "📌 모든 GPS 데이터 조회", description = "저장된 모든 GPS 데이터를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
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
