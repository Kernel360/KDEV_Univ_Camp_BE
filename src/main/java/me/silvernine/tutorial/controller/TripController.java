package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.dto.TripRequestDto;
import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        trip.setTimestamp(tripRequestDto.getTimestamp());
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
            trip.setTimestamp(dto.getTimestamp());
            trip.setLatitude(dto.getLatitude());
            trip.setLongitude(dto.getLongitude());
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
}
