package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.BatchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BatchDataRepository extends JpaRepository<BatchData, Long> {

    @Query("SELECT d FROM BatchData d WHERE d.timestamp >= :start AND d.timestamp < :end")
    List<BatchData> findDataForDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
