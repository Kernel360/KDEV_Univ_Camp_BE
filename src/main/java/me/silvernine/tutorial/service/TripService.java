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

    public List<Trip> getAllTripsByCarNumber(String carNumber) {
        return tripRepository.findByCarNumber(carNumber);
    }

    public List<Trip> getAllTripsByCarNumberWithInterval(String carNumber, int interval) {
        return tripRepository.findByCarNumberAndIntervalForAllDates(carNumber, interval);
    }

    public List<Trip> getTripsByCarNumberAndTimestampBetween(String carNumber, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return tripRepository.findByCarNumberAndTimestampBetween(carNumber, startDateTime, endDateTime);
    }

    public List<Trip> getTripsByCarNumberAndInterval(String carNumber, LocalDateTime startDateTime, LocalDateTime endDateTime, int interval) {
        return tripRepository.findByCarNumberAndInterval(carNumber, startDateTime, endDateTime, interval);
    }
}
