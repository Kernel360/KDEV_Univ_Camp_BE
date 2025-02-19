package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.BatchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<BatchData, Long> {
    // ✅ 'timestamp' 기준으로 특정 날짜의 데이터 조회
    List<BatchData> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
