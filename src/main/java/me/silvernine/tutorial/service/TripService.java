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

    // ✅ 단일 데이터 저장 (기존 코드 유지)
    public Trip saveTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    // ✅ 배치 데이터 저장 (단일 저장 로직과 동일하게 적용)
    public void saveTrips(List<Trip> tripList) {
        tripList.forEach(trip -> {
            if (trip.getTimestamp() == null || trip.getTimestamp().isEmpty()) {
                trip.setTimestamp("1970-01-01 00:00:00.000000"); // 기본값 설정
            }
        });

        tripRepository.saveAll(tripList);
    }

    // ✅ 특정 시간 이후 데이터 조회 (기존 코드 유지)
    public List<Trip> getRecentTrips(String since) {
        return tripRepository.findByTimestampAfter(since);
    }
}
