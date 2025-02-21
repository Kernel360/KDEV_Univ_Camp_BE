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

    // ✅ 특정 차량의 전체 GPS 데이터 조회 (interval 없이 모든 데이터)
    public List<Trip> getTripsByCarNumberAndTimestampBetween(String carNumber, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return tripRepository.findByCarNumberAndTimestampBetween(carNumber, startDateTime, endDateTime);
    }

    // ✅ 특정 차량의 interval 간격으로 필터링된 GPS 데이터 조회
    public List<Trip> getTripsByCarNumberAndInterval(String carNumber, LocalDateTime startDateTime, LocalDateTime endDateTime, int interval) {
        return tripRepository.findByCarNumberAndInterval(carNumber, startDateTime, endDateTime, interval);
    }
}
