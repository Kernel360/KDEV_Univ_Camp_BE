package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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
    @Query(value = """
    SELECT * FROM trip_data
    WHERE car_number = :carNumber 
    AND timestamp BETWEEN :startDateTime AND :endDateTime
    AND UNIX_TIMESTAMP(timestamp) % :interval = 0
    ORDER BY timestamp
    """, nativeQuery = true)
    List<Trip> findByCarNumberAndInterval(
            @Param("carNumber") String carNumber,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("interval") int interval);

    // ✅ 특정 차량의 기록된 날짜 목록 조회 (추가된 메서드)
    @Query("SELECT DISTINCT DATE(t.timestamp) FROM Trip t WHERE t.carNumber = :carNumber ORDER BY DATE(t.timestamp)")
    List<LocalDate> findDistinctDatesByCarNumber(@Param("carNumber") String carNumber);
}
