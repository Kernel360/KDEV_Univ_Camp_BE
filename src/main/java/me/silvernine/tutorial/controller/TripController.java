package me.silvernine.tutorial.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import me.silvernine.tutorial.entity.TripData;
import me.silvernine.tutorial.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Trip API", description = "99_course_trip.txt 데이터를 처리하는 API")
@RestController
@RequestMapping("/api/trip")
public class TripController {

    @Autowired
    private TripService tripService;

    @PostMapping("/upload")
    public String uploadTripData(@RequestBody List<TripData> tripDataList) {
        tripService.saveTripData(tripDataList);
        return "Trip data successfully stored!";
    }
}
