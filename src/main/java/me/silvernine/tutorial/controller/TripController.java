package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.dto.TripRequestDto;
import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.service.TripService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trip")
public class TripController {

    private final TripService tripService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"); // 🔥 6자리 소수점 처리

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // ✅ 단일 데이터 저장 (JWT 인증 추가)
    @PostMapping
    public ResponseEntity<Trip> saveTrip(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                         @RequestBody TripRequestDto tripRequestDto) {
        checkAuthentication(authorization); // 🔥 JWT 인증 확인
        Trip trip = convertToTrip(tripRequestDto);
        Trip savedTrip = tripService.saveTrip(trip);
        return ResponseEntity.ok(savedTrip);
    }

    // ✅ 배치 데이터 저장 (JWT 인증 추가)
    @PostMapping("/batch")
    public ResponseEntity<?> saveTrips(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                       @RequestBody List<TripRequestDto> tripRequestDtos) {
        checkAuthentication(authorization); // 🔥 JWT 인증 확인
        List<Trip> trips = tripRequestDtos.stream()
                .map(this::convertToTrip)
                .collect(Collectors.toList());

        tripService.saveTrips(trips);
        return ResponseEntity.ok().body("Batch data saved successfully");
    }

    // ✅ 최근 데이터 조회 (JWT 인증 추가)
    @GetMapping("/recent")
    public ResponseEntity<List<Trip>> getRecentTrips(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
                                                     @RequestParam LocalDateTime since) {
        checkAuthentication(authorization); // 🔥 JWT 인증 확인
        return ResponseEntity.ok(tripService.getRecentTrips(since));
    }

    // ✅ TripRequestDto → Trip 변환 메서드 (단일 & 배치 공통)
    private Trip convertToTrip(TripRequestDto dto) {
        if (dto.getTime() == null || dto.getTime().trim().isEmpty()) {
            throw new IllegalArgumentException("🚨 time 값이 없습니다. 요청 데이터: " + dto);
        }

        Trip trip = new Trip();
        trip.setVehicleId(dto.getVehicleId());
        trip.setLatitude(dto.getLatitude());
        trip.setLongitude(dto.getLongitude());

        // 🔥 time 값 변환 시 소수점 6자리 처리
        String timeStr = dto.getTime().trim();
        try {
            if (timeStr.contains(".")) {
                String[] parts = timeStr.split("\\.");
                timeStr = parts[0] + "." + parts[1].substring(0, Math.min(parts[1].length(), 6));
                while (timeStr.length() < 26) { // 소수점 부족하면 0으로 채움
                    timeStr += "0";
                }
            } else {
                timeStr += ".000000"; // 소수점이 없으면 6자리로 변환
            }

            trip.setTimestamp(LocalDateTime.parse(timeStr, formatter));
        } catch (Exception e) {
            throw new IllegalArgumentException("🚨 timestamp 변환 실패: " + timeStr, e);
        }

        trip.setBatteryLevel(100); // 기본 배터리 값 설정
        return trip;
    }

    // ✅ JWT 인증 검증 (Authorization 헤더 확인)
    private void checkAuthentication(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new SecurityException("🚨 JWT 토큰이 없습니다.");
        }

        String token = authorization.substring(7); // "Bearer " 이후 토큰 값 추출
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetails)) {
            throw new SecurityException("🚨 유효하지 않은 JWT 토큰입니다.");
        }
    }
}
