package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.dto.TripRequestDto;
import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.service.TripService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    // ✅ 배치 데이터 저장
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

    // ✅ 차량 번호 + 기간별 GPS 정보 조회
    @GetMapping("/search")
    public ResponseEntity<List<Trip>> searchTrips(
            @RequestParam String vehicleId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        // 종료 날짜(endDate)가 없으면 시작 날짜만 검색
        if (endDate == null) {
            endDate = startDate;
        }

        // 시작일 00:00:00, 종료일 23:59:59.99 설정
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999_999_999);

        List<Trip> trips = tripService.getTripsByVehicleAndDateRange(vehicleId, startDateTime, endDateTime);
        return ResponseEntity.ok(trips);
    }

    // ✅ TripRequestDto → Trip 변환 메서드 (단일 & 배치 공통)
    private Trip convertToTrip(TripRequestDto dto) {
        Trip trip = new Trip();
        trip.setVehicleId(dto.getVehicleId());
        trip.setLatitude(dto.getLatitude());
        trip.setLongitude(dto.getLongitude());

        // ✅ 'time' 값을 LocalDateTime으로 변환
        trip.setTimestamp(LocalDateTime.parse(dto.getTime(), formatter));

        // ✅ 배터리 값 반영 (null이면 100으로 설정)
        trip.setBatteryLevel(dto.getBatteryLevel() != null ? dto.getBatteryLevel() : 100);

        return trip;
    }
}
