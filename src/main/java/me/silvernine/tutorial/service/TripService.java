package me.silvernine.tutorial.service;

import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.repository.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TripService {

    private final TripRepository tripRepository;

    @PersistenceContext
    private EntityManager entityManager;  // ✅ EntityManager 추가

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    // ✅ GPS 데이터 저장 (스케줄링 실행)
    public void saveGpsData() {
        try {
            Trip trip = new Trip();
            trip.setVehicleId("12가 1234"); // 특정 차량 ID
            trip.setLatitude(37.5665); // 샘플 데이터
            trip.setLongitude(126.9780);
            trip.setTimestamp(LocalDateTime.now());
            tripRepository.save(trip);

            System.out.println("✅ [Scheduled] GPS 데이터 저장 완료: " + trip.getTimestamp());
        } catch (Exception e) {
            System.err.println("🚨 [Scheduled] GPS 데이터 저장 중 오류 발생: " + e.getMessage());
        }
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

    // ✅ 여러 개 데이터 저장 (Batch Insert) - 트랜잭션 적용
    @Transactional
    public void saveTrips(List<Trip> trips) {
        try {
            tripRepository.saveAll(trips);
            entityManager.flush();  // ✅ 즉시 DB 반영

            System.out.println("✅ [Batch Insert] 총 " + trips.size() + "개의 데이터 저장 완료!");
        } catch (Exception e) {
            System.err.println("🚨 [Batch Insert] 데이터 저장 실패: " + e.getMessage());
        }
    }

    // ✅ 모든 데이터 조회
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    // ✅ 특정 차량의 최근 GPS 데이터 조회
    public List<Trip> getLatestGpsDataByVehicle(String vehicleId) {
        return tripRepository.findTopByVehicleIdContainingOrderByTimestampDesc(vehicleId)
                .map(Collections::singletonList) // 단일 결과를 리스트로 변환
                .orElse(Collections.emptyList()); // 데이터 없을 경우 빈 리스트 반환
    }

    // ✅ 특정 차량의 일정 기간 내 GPS 데이터 조회
    public List<Trip> getGpsDataByVehicleAndTime(String vehicleId, LocalDateTime startTime, LocalDateTime endTime) {
        return tripRepository.findByVehicleIdAndTimestampBetween(vehicleId, startTime, endTime);
    }
}
