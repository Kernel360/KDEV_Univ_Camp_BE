package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByTimestampAfter(LocalDateTime timestamp);

    @Query("SELECT t FROM Trip t WHERE t.carNumber = :carNumber " +
            "AND t.timestamp BETWEEN :startDateTime AND :endDateTime " +
            "ORDER BY t.timestamp")
    List<Trip> findByCarNumberAndTimestampBetween(@Param("carNumber") String carNumber,
                                                  @Param("startDateTime") LocalDateTime startDateTime,
                                                  @Param("endDateTime") LocalDateTime endDateTime);

    @Query(value = """
    SELECT * FROM trip_data
    WHERE car_number = :carNumber 
    AND timestamp BETWEEN :startDateTime AND :endDateTime
    AND UNIX_TIMESTAMP(timestamp) % :interval = 0
    ORDER BY timestamp
    """, nativeQuery = true)
    List<Trip> findByCarNumberAndInterval(@Param("carNumber") String carNumber,
                                          @Param("startDateTime") LocalDateTime startDateTime,
                                          @Param("endDateTime") LocalDateTime endDateTime,
                                          @Param("interval") int interval);

    // ✅ 차량 번호별 기록된 날짜 조회 쿼리 추가
    @Query("SELECT DISTINCT DATE(t.timestamp) FROM Trip t WHERE t.carNumber = :carNumber ORDER BY DATE(t.timestamp)")
    List<LocalDate> findDistinctDatesByCarNumber(@Param("carNumber") String carNumber);
}
