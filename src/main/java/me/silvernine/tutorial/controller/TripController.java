package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.dto.TripRequestDto;
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

    @PostMapping
    public ResponseEntity<Trip> saveTrip(@RequestBody TripRequestDto tripRequestDto) {
        Trip trip = tripService.saveTrip(tripRequestDto);
        return ResponseEntity.ok(trip);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Trip>> saveTrips(@RequestBody List<TripRequestDto> tripRequestDtoList) {
        List<Trip> savedTrips = tripService.saveTrips(tripRequestDtoList);
        return ResponseEntity.ok(savedTrips);
    }
}
