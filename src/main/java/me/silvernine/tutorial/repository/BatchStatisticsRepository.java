package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.BatchStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchStatisticsRepository extends JpaRepository<BatchStatistics, Long> {
}
