package me.silvernine.tutorial.repository;

import me.silvernine.tutorial.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> { // ✅ ID 타입 변경 (Long → String)
    Vehicle findByVehicleId(String vehicleId); // ✅ 차량 ID로 조회
    boolean existsByVehicleId(String vehicleId); // ✅ 중복 확인
    boolean existsByRegistrationNumber(String registrationNumber); // ✅ 차량 번호 중복 확인
}
