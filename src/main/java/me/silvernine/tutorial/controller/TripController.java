package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
                .map(this::convertToTrip)
                .collect(Collectors.toList());

        tripService.saveTrips(trips);
        return ResponseEntity.ok().body("Batch data saved successfully");
    }

    // ✅ 최근 데이터 조회
    @GetMapping("/recent")
    public ResponseEntity<List<Trip>> getRecentTrips(@RequestParam LocalDateTime since) {
        return ResponseEntity.ok(tripService.getRecentTrips(since));
    }

    // ✅ 차량 번호 + 기간별 GPS 정보 조회 (주기 적용)
    @Operation(summary = "차량 번호 + 기간별 Trip 데이터 조회", description = "특정 차량의 위치 데이터를 특정 기간 동안 조회합니다. 주기를 설정하면 해당 간격으로 데이터를 필터링합니다.")
    @GetMapping("/search")
    public ResponseEntity<List<Trip>> searchTrips(
            @Parameter(description = "차량 번호 (예: 12가1234)", required = true, example = "12가1234")
            @RequestParam String carNumber,

            @Parameter(description = "검색 시작 날짜 (yyyy-MM-dd)", required = true, example = "2025-01-01")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,

            @Parameter(description = "검색 종료 날짜 (yyyy-MM-dd) [선택]", required = false, example = "2025-02-01")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,

            @Parameter(description = "주기 (초 단위, 예: 60, 120, 180) [선택]", required = false, example = "60")
            @RequestParam(required = false) Integer interval) {

        if (endDate == null) {
            endDate = startDate;
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999_999_999);

        List<Trip> trips;

        if (interval != null && (interval == 60 || interval == 120 || interval == 180)) {
            // ✅ interval이 지정된 경우 주기 적용
            trips = tripService.getTripsByCarNumberAndInterval(carNumber, startDateTime, endDateTime, interval);
        } else {
            // ✅ interval이 없으면 기존 방식으로 조회
            trips = tripService.getTripsByCarNumberAndTimestampBetween(carNumber, startDateTime, endDateTime);
        }

        return ResponseEntity.ok(trips);
    }



    // ✅ TripRequestDto → Trip 변환 메서드
    private Trip convertToTrip(TripRequestDto dto) {
        Trip trip = new Trip();

        // ✅ 기존 `vehicleId` → `carNumber` 변경
        trip.setCarNumber(dto.getCarNumber());

        trip.setLatitude(dto.getLatitude());
        trip.setLongitude(dto.getLongitude());

        // ✅ 'time' 값을 LocalDateTime으로 변환 (.SS 포맷 유지)
        trip.setTimestamp(LocalDateTime.parse(dto.getTime(), formatter));

        // ✅ 배터리 값 반영 (null이면 100으로 설정)
        trip.setBatteryLevel(dto.getBatteryLevel() != null ? dto.getBatteryLevel() : 100);

        return trip;
    }
}
