package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    // 차량 ID로 조회하는 메서드 추가
    Vehicle findByVehicleId(String vehicleId);
}
