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

    // ✅ GPS 데이터 저장 (스케줄링 실행)
    public void saveGpsData() {
        Trip trip = new Trip();
        trip.setVehicleId("1234"); // 특정 차량 ID
        trip.setLatitude(37.5665); // 샘플 데이터
        trip.setLongitude(126.9780);
        trip.setTimestamp(LocalDateTime.now());
        tripRepository.save(trip);
    }

    // ✅ 특정 주기(60초, 120초, 180초)마다 저장된 데이터 조회
    public List<Trip> getTripsByInterval(int interval) {
        LocalDateTime startTime = LocalDateTime.now().minusSeconds(interval);
        return tripRepository.findByTimestampAfter(startTime);
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

    // ✅ 특정 차량의 최근 GPS 데이터 조회
    public List<Trip> getLatestGpsDataByVehicle(String vehicleId) {
        return tripRepository.findTopByVehicleIdOrderByTimestampDesc(vehicleId);
    }

    // ✅ 특정 차량의 일정 기간 내 GPS 데이터 조회
    public List<Trip> getGpsDataByVehicleAndTime(String vehicleId, LocalDateTime startTime, LocalDateTime endTime) {
        return tripRepository.findByVehicleIdAndTimestampBetween(vehicleId, startTime, endTime);
    }
}
