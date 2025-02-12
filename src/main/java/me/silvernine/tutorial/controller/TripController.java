package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ResponseEntity<Trip> saveTrip(@RequestBody TripRequestDto tripRequestDto) {
        Trip trip = new Trip();
        trip.setVehicleId(tripRequestDto.getVehicleId());

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime timestamp = LocalDateTime.parse(tripRequestDto.getTimestamp(), formatter);
            trip.setTimestamp(timestamp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

        trip.setLatitude(tripRequestDto.getLatitude());
        trip.setLongitude(tripRequestDto.getLongitude());

        Trip savedTrip = tripService.saveTrip(trip);
        return ResponseEntity.ok(savedTrip);
    }

    @Operation(summary = "📌 배치 GPS 데이터 저장", description = "🚗 여러 개의 GPS 데이터를 한 번에 저장합니다.")
    @PostMapping("/batch")
    public ResponseEntity<?> saveTrips(@RequestBody List<TripRequestDto> tripRequestDtos) {
        List<Trip> trips = tripRequestDtos.stream().map(dto -> {
            Trip trip = new Trip();
            trip.setVehicleId(dto.getVehicleId());

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime timestamp = LocalDateTime.parse(dto.getTimestamp(), formatter);
                trip.setTimestamp(timestamp);
            } catch (Exception e) {
                return null;
            }

            trip.setLatitude(dto.getLatitude());
            trip.setLongitude(dto.getLongitude());
            trip.setBatteryLevel(dto.getBatteryLevel());
            return trip;
        }).filter(trip -> trip != null).collect(Collectors.toList());

        tripService.saveTrips(trips);
        return ResponseEntity.ok().body("Data saved successfully");
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
