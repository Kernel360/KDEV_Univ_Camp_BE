package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.dto.TripRequestDto;
import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*") // ✅ 모든 도메인에서 API 요청 가능
@RestController
@RequestMapping("/api/trip")
public class TripController {

    @Autowired
    private TripService tripService;

    // ✅ 주기 설정을 저장할 변수 (기본값: 60초)
    private static int tripInterval = 60;

    // ✅ 사용자가 주기 선택 (60, 120, 180초)
    @PostMapping("/setFrequency")
    public ResponseEntity<String> setFrequency(@RequestParam int interval) {
        if (interval == 60 || interval == 120 || interval == 180) {
            tripInterval = interval;
            return ResponseEntity.ok("주기 설정 완료: " + interval + "초");
        } else {
            return ResponseEntity.badRequest().body("잘못된 주기 값입니다. 60, 120, 180 중 선택하세요.");
        }
    }

    // ✅ 주기적으로 GPS 데이터 저장 (백그라운드 실행)
    @Scheduled(fixedRateString = "#{T(java.lang.Integer).parseInt(@tripController.tripInterval) * 1000}") // 초 단위로 동작
    public void saveGpsDataScheduled() {
        tripService.saveGpsData();
        System.out.println("자동 저장 실행됨 (주기: " + tripInterval + "초)");
    }

    // ✅ 단일 데이터 저장
    @PostMapping
    public ResponseEntity<Trip> saveTrip(@RequestBody TripRequestDto tripRequestDto) {
        Trip trip = new Trip();
        trip.setVehicleId(tripRequestDto.getVehicleId());

        // ✅ String → LocalDateTime 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timestamp = LocalDateTime.parse(tripRequestDto.getTimestamp(), formatter);
        trip.setTimestamp(timestamp); // ✅ 변환 후 저장

        trip.setLatitude(tripRequestDto.getLatitude());
        trip.setLongitude(tripRequestDto.getLongitude());

        Trip savedTrip = tripService.saveTrip(trip);
        return ResponseEntity.ok(savedTrip);
    }

    // ✅ 여러 개의 데이터 저장 (Batch Insert)
    @PostMapping("/batch")
    public ResponseEntity<?> saveTrips(@RequestBody List<TripRequestDto> tripRequestDtos) {
        List<Trip> trips = tripRequestDtos.stream().map(dto -> {
            Trip trip = new Trip();
            trip.setVehicleId(dto.getVehicleId());

            // ✅ String → LocalDateTime 변환
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime timestamp = LocalDateTime.parse(dto.getTimestamp(), formatter);
            trip.setTimestamp(timestamp); // ✅ 변환 후 저장

            trip.setLatitude(dto.getLatitude());
            trip.setLongitude(dto.getLongitude());
            trip.setBatteryLevel(dto.getBatteryLevel());
            return trip;
        }).collect(Collectors.toList());

        tripService.saveTrips(trips);
        return ResponseEntity.ok().body("Data saved successfully");
    }

    // ✅ 모든 데이터 조회
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    // ✅ 주기별 GPS 데이터 조회 API
    @GetMapping("/gpsData")
    public ResponseEntity<List<Trip>> getGpsData(@RequestParam int interval) {
        if (interval == 60 || interval == 120 || interval == 180) {
            List<Trip> trips = tripService.getTripsByInterval(interval);
            return ResponseEntity.ok(trips);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // ✅ 특정 차량의 최근 GPS 데이터 조회 API
    @GetMapping("/latestGpsData")
    public ResponseEntity<List<Trip>> getLatestGpsData(@RequestParam String vehicleId) {
        return ResponseEntity.ok(tripService.getLatestGpsDataByVehicle(vehicleId));
    }
}
