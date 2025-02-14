package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.dto.TripRequestDto;
import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trip")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // ✅ 단일 저장 요청 (유지)
    @PostMapping
    public ResponseEntity<Trip> saveTrip(@RequestBody TripRequestDto tripRequestDto) {
        Trip savedTrip = tripService.saveTrip(tripRequestDto);
        return ResponseEntity.ok(savedTrip);
    }

    // ✅ 배치 저장 요청 (변경)
    @PostMapping("/batch")
    public ResponseEntity<?> saveTrips(@RequestBody List<TripRequestDto> tripRequestDtoList) {
        List<Trip> trips = tripRequestDtoList.stream()
                .map(tripService::convertToEntity) // ✅ DTO를 Entity로 변환
                .collect(Collectors.toList());

        List<Trip> savedTrips = tripService.saveTrips(trips);
        return ResponseEntity.ok(savedTrips);
    }
}
