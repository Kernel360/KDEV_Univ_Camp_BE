package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.dto.TripRequestDto;
import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trip")
public class TripController {

    private final TripService tripService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS");

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // ✅ 단일 데이터 저장
    @PostMapping
    public ResponseEntity<Trip> saveTrip(@RequestBody TripRequestDto tripRequestDto) {
        Trip trip = convertToTrip(tripRequestDto);
        Trip savedTrip = tripService.saveTrip(trip);
        return ResponseEntity.ok(savedTrip);
    }

    // ✅ 배치 데이터 저장 (🚀 단일 저장과 동일한 변환 방식 적용)
    @PostMapping("/batch")
    public ResponseEntity<?> saveTrips(@RequestBody List<TripRequestDto> tripRequestDtos) {
        List<Trip> trips = tripRequestDtos.stream()
                .map(this::convertToTrip) // 🔥 각 DTO를 Trip 객체로 변환
                .collect(Collectors.toList());

        tripService.saveTrips(trips);
        return ResponseEntity.ok().body("Batch data saved successfully");
    }

    // ✅ 최근 데이터 조회
    @GetMapping("/recent")
    public ResponseEntity<List<Trip>> getRecentTrips(@RequestParam LocalDateTime since) {
        return ResponseEntity.ok(tripService.getRecentTrips(since));
    }

    // ✅ TripRequestDto → Trip 변환 메서드 (단일 & 배치 공통)
    private Trip convertToTrip(TripRequestDto dto) {
        Trip trip = new Trip();
        trip.setVehicleId(dto.getVehicleId()); // 🔥 수정된 DTO 필드 사용
        trip.setLatitude(dto.getLatitude());
        trip.setLongitude(dto.getLongitude());

        // 🔥 'time' 값을 LocalDateTime으로 변환
        trip.setTimestamp(LocalDateTime.parse(dto.getTime(), formatter));

        // 🔥 기본 배터리 값 설정 (100부터 시작)
        trip.setBatteryLevel(100);

        return trip;
    }
}
