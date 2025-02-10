package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByOwnerUsername(String ownerUsername);
    Optional<Car> findByCarNumber(String carNumber);
}