package me.silvernine.tutorial.service;

import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.repository.TripRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    // ✅ 단일 데이터 저장
    public Trip saveTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    // ✅ 배치 데이터 저장
    public void saveTrips(List<Trip> tripList) {
        tripRepository.saveAll(tripList);
    }

    // ✅ 특정 시간 이후 데이터 조회
    public List<Trip> getRecentTrips(String since) {
        return tripRepository.findByTimestampAfter(since);
    }
}
