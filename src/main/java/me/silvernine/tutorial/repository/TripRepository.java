package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.TripData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<TripData, Long> {
}
