package me.silvernine.tutorial.service;

import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public Trip saveTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public void saveTrips(List<Trip> trips) {
        tripRepository.saveAll(trips);
    }

    public List<Trip> getRecentTrips(LocalDateTime since) {
        return tripRepository.findByTimestampAfter(since);
    }
}
