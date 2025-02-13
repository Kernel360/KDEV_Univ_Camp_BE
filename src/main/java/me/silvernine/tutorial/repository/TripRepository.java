package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    // ✅ 특정 시간 이후의 데이터 조회
    List<Trip> findByTimestampAfter(LocalDateTime timestamp);

    // ✅ 특정 차량의 가장 최근 GPS 데이터 조회
    Optional<Trip> findTopByVehicleIdContainingOrderByTimestampDesc(String vehicleId);

    // ✅ 특정 차량의 일정 기간 내 GPS 데이터 조회
    List<Trip> findByVehicleIdAndTimestampBetween(String vehicleId, LocalDateTime startTime, LocalDateTime endTime);

    // ✅ Native Query를 활용한 Batch Insert 최적화
    @Modifying
    @Query(value = "INSERT INTO trip_data (vehicle_id, timestamp, latitude, longitude, battery_level) VALUES (:vehicleId, :timestamp, :latitude, :longitude, :batteryLevel)", nativeQuery = true)
    void insertTrip(String vehicleId, LocalDateTime timestamp, Double latitude, Double longitude, Integer batteryLevel);
}
