package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    // ✅ 특정 시간 이후의 데이터 조회
    List<Trip> findByTimestampAfter(LocalDateTime timestamp);

    // ✅ 차량 번호 + 특정 기간의 전체 GPS 데이터 조회 (interval 없이 모든 데이터)
    @Query("SELECT t FROM Trip t WHERE t.carNumber = :carNumber " +
            "AND t.timestamp BETWEEN :startDateTime AND :endDateTime " +
            "ORDER BY t.timestamp")
    List<Trip> findByCarNumberAndTimestampBetween(
            @Param("carNumber") String carNumber,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);

    // ✅ 차량 번호 + 특정 기간의 GPS 데이터 중 interval 간격으로 필터링된 데이터 조회
    @Query("SELECT t FROM Trip t WHERE t.carNumber = :carNumber " +
            "AND t.timestamp BETWEEN :startDateTime AND :endDateTime " +
            "AND MOD(FUNCTION('UNIX_TIMESTAMP', t.timestamp), :interval) = 0 " +
            "ORDER BY t.timestamp")
    List<Trip> findByCarNumberAndInterval(
            @Param("carNumber") String carNumber,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("interval") int interval);

}
