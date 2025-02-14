package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.model.TripData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<TripData, Long> {
    List<TripData> findByTimestampAfter(LocalDateTime timestamp); // LocalDateTime 비교
}
