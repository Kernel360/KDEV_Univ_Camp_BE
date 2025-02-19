package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    // ✅ 기존 기능 유지: 특정 시간 이후의 데이터 조회
    List<Trip> findByTimestampAfter(LocalDateTime timestamp);

    // ✅ 차량 번호 + 특정 기간의 위도 & 경도만 조회
    @Query("SELECT t FROM Trip t WHERE t.vehicleId = :vehicleId AND t.timestamp BETWEEN :startDateTime AND :endDateTime")
    List<Trip> findByVehicleIdAndTimestampBetween(
            @Param("vehicleId") String vehicleId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
}
