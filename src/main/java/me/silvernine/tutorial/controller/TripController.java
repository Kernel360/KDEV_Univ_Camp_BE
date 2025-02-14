package me.silvernine.tutorial.controller;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.model.TripData;
import me.silvernine.tutorial.service.TripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/trip")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<TripData> saveTrip(@RequestBody TripData tripData) {
        TripData savedTrip = tripService.saveTrip(tripData);
        return ResponseEntity.ok(savedTrip);
    }

    @PostMapping("/batch")
    public ResponseEntity<?> saveTrips(@RequestBody List<TripData> tripDataList) {
        tripService.saveTrips(tripDataList);
        return ResponseEntity.ok().body("Data saved successfully");
    }

    @GetMapping("/recent")
    public ResponseEntity<List<TripData>> getRecentTrips(@RequestParam LocalDateTime since) {
        return ResponseEntity.ok(tripService.getRecentTrips(since));
    }
}
