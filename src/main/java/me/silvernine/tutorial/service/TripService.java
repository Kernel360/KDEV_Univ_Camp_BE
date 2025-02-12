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

    // ✅ 여러 개 데이터 저장 (Batch Insert)
    public void saveTrips(List<Trip> trips) {
        tripRepository.saveAll(trips);
    }

    // ✅ 모든 데이터 조회
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }
}
