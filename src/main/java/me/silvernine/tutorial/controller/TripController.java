package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trip")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // ✅ 단일 데이터 저장 API (기존 코드 유지)
    @PostMapping
    public ResponseEntity<Trip> saveTrip(@RequestBody Trip trip) {
        Trip savedTrip = tripService.saveTrip(trip);
        return ResponseEntity.ok(savedTrip);
    }

    // ✅ 배치 데이터 저장 API (단일 저장 방식과 동일하게 수정)
    @PostMapping("/batch")
    public ResponseEntity<?> saveTrips(@RequestBody List<Trip> tripList) {
        tripList.forEach(trip -> {
            // ✅ timestamp 변환 (기존 단일 데이터 로직 유지)
            if (trip.getTimestamp() == null || trip.getTimestamp().isEmpty()) {
                trip.setTimestamp("1970-01-01 00:00:00.000000"); // 기본값 설정
            }
        });

        tripService.saveTrips(tripList);
        return ResponseEntity.ok("Batch data saved successfully");
    }

    // ✅ 최근 데이터 조회 API (기존 유지)
    @GetMapping("/recent")
    public ResponseEntity<List<Trip>> getRecentTrips(@RequestParam String since) {
        return ResponseEntity.ok(tripService.getRecentTrips(since));
    }
}
