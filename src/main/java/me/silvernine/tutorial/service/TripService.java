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

@Service
public class TripService {

    private final TripRepository tripRepository;

    @PersistenceContext
    private EntityManager entityManager;  // ✅ EntityManager 추가

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    // ✅ GPS 데이터 저장 (스케줄링 실행)
    @Transactional
    public void saveGpsData() {
        try {
            Trip trip = new Trip();
            trip.setVehicleId("12가 1234");
            trip.setLatitude(37.5665);
            trip.setLongitude(126.9780);
            trip.setTimestamp(LocalDateTime.now());
            tripRepository.save(trip);
            entityManager.flush(); // 즉시 반영

            System.out.println("✅ [Scheduled] GPS 데이터 저장 완료: " + trip.getTimestamp());
        } catch (Exception e) {
            System.err.println("🚨 [Scheduled] GPS 데이터 저장 실패: " + e.getMessage());
        }
    }

    // ✅ 특정 주기(60초, 120초, 180초)마다 저장된 데이터 조회
    public List<Trip> getTripsByInterval(int interval) {
        LocalDateTime startTime = LocalDateTime.now().minusSeconds(interval);
        return tripRepository.findByTimestampAfter(startTime);
    }

    // ✅ 단일 데이터 저장
    @Transactional
    public Trip saveTrip(Trip trip) {
        try {
            Trip savedTrip = tripRepository.save(trip);
            entityManager.flush();  // ✅ 즉시 반영
            System.out.println("✅ [Single Insert] 데이터 저장 완료: " + savedTrip.getTimestamp());
            return savedTrip;
        } catch (Exception e) {
            System.err.println("🚨 [Single Insert] 데이터 저장 실패: " + e.getMessage());
            throw e;
        }
    }

    // ✅ 여러 개 데이터 저장 (Batch Insert) - 트랜잭션 적용
    @Transactional(rollbackFor = Exception.class)  // ✅ 예외 발생 시 롤백되도록 설정
    public void saveTrips(List<Trip> trips) {
        try {
            if (trips.isEmpty()) {
                System.err.println("🚨 [Batch Insert] 저장할 데이터가 없음!");
                return;
            }

            tripRepository.saveAll(trips);
            entityManager.flush();  // ✅ 즉시 DB 반영

            System.out.println("✅ [Batch Insert] 총 " + trips.size() + "개의 데이터 저장 완료!");
        } catch (Exception e) {
            System.err.println("🚨 [Batch Insert] 데이터 저장 실패: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }



    // ✅ 모든 데이터 조회
    public List<Trip> getAllTrips() {
        try {
            List<Trip> trips = tripRepository.findAll();
            System.out.println("✅ [Get All Trips] 총 " + trips.size() + "개의 데이터 조회 완료!");
            return trips;
        } catch (Exception e) {
            System.err.println("🚨 [Get All Trips] 데이터 조회 실패: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // ✅ 특정 차량의 최근 GPS 데이터 조회
    public List<Trip> getLatestGpsDataByVehicle(String vehicleId) {
        try {
            return tripRepository.findTopByVehicleIdContainingOrderByTimestampDesc(vehicleId)
                    .map(Collections::singletonList) // 단일 결과를 리스트로 변환
                    .orElse(Collections.emptyList()); // 데이터 없을 경우 빈 리스트 반환
        } catch (Exception e) {
            System.err.println("🚨 [Get Latest GPS] 데이터 조회 실패: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // ✅ 특정 차량의 일정 기간 내 GPS 데이터 조회
    public List<Trip> getGpsDataByVehicleAndTime(String vehicleId, LocalDateTime startTime, LocalDateTime endTime) {
        try {
            return tripRepository.findByVehicleIdAndTimestampBetween(vehicleId, startTime, endTime);
        } catch (Exception e) {
            System.err.println("🚨 [Get GPS By Time] 데이터 조회 실패: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
