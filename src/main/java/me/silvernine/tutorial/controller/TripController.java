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

@CrossOrigin(origins = "*") // ✅ 모든 도메인에서 API 요청 가능
@RestController
@RequestMapping("/api/trip")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // ✅ 단일 데이터 저장
    @PostMapping
    public ResponseEntity<Trip> saveTrip(@RequestBody TripRequestDto tripRequestDto) {
        Trip trip = new Trip();
        trip.setVehicleId(tripRequestDto.getVehicleId());

        try {
            // ✅ `date` + `time` 값을 LocalDateTime으로 변환
            String datePart = (tripRequestDto.getDate() == null || tripRequestDto.getDate().isEmpty()) ? "1970-01-01" : tripRequestDto.getDate();
            String fixedTimestamp = (datePart + " " + tripRequestDto.getTimestamp()).replace(".00", "").trim();
            DateTimeFormatter formatter = fixedTimestamp.contains(".")
                    ? DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS") // 밀리초 포함
                    : DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 밀리초 없음
            LocalDateTime parsedTimestamp = LocalDateTime.parse(fixedTimestamp, formatter);
            trip.setTimestamp(parsedTimestamp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

        trip.setLatitude(tripRequestDto.getLatitude());
        trip.setLongitude(tripRequestDto.getLongitude());
        trip.setBatteryLevel(tripRequestDto.getBatteryLevel());

        Trip savedTrip = tripService.saveTrip(trip);
        return ResponseEntity.ok(savedTrip);
    }

    // ✅ 여러 개의 데이터 저장 (Batch Insert)
    @PostMapping("/batch")
    public ResponseEntity<?> saveTrips(@RequestBody List<TripRequestDto> tripRequestDtos) {
        List<Trip> trips = tripRequestDtos.stream().map(dto -> {
            Trip trip = new Trip();
            trip.setVehicleId(dto.getVehicleId());

            try {
                // ✅ `date` + `time` 값을 LocalDateTime으로 변환
                String datePart = (dto.getDate() == null || dto.getDate().isEmpty()) ? "1970-01-01" : dto.getDate();
                String fixedTimestamp = (datePart + " " + dto.getTimestamp()).replace(".00", "").trim();
                DateTimeFormatter formatter = fixedTimestamp.contains(".")
                        ? DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS") // 밀리초 포함
                        : DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 밀리초 없음
                LocalDateTime timestamp = LocalDateTime.parse(fixedTimestamp, formatter);
                trip.setTimestamp(timestamp);
            } catch (Exception e) {
                return null;
            }

            trip.setLatitude(dto.getLatitude());
            trip.setLongitude(dto.getLongitude());
            trip.setBatteryLevel(dto.getBatteryLevel());

            return trip;
        }).filter(trip -> trip != null).collect(Collectors.toList());

        if (trips.isEmpty()) {
            return ResponseEntity.badRequest().body("Error parsing timestamps");
        }

        tripService.saveTrips(trips);
        return ResponseEntity.ok().body("Data saved successfully");
    }

    // ✅ 모든 데이터 조회
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }
}
