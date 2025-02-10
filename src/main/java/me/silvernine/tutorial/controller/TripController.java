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

    // 데이터 저장 API
    @PostMapping
    public ResponseEntity<Trip> saveTrip(@RequestBody Trip trip) {
        Trip savedTrip = tripService.saveTrip(trip);
        return ResponseEntity.ok(savedTrip);
    }

    // 저장된 데이터 조회 API
    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }
}
