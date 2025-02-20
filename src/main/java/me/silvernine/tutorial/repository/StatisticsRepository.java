package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.BatchStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface StatisticsRepository extends JpaRepository<BatchStatistics, Long> {
    boolean existsByTimestamp(LocalDateTime timestamp); // ✅ 중복 방지 메서드 추가
}
