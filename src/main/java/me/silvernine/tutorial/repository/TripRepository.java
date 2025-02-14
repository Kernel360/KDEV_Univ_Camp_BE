package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByTimestampAfter(LocalDateTime timestamp);
}
