package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.VehicleEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<VehicleEvent, Long> {
}