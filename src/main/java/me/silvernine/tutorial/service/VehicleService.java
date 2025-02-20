package me.silvernine.tutorial.service;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.entity.Vehicle;
import me.silvernine.tutorial.repository.VehicleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    public Vehicle saveVehicle(Vehicle vehicle) {
        // ✅ 중복 등록 방지
        if (vehicleRepository.existsByVehicleId(vehicle.getVehicleId())) {
            throw new IllegalArgumentException("이미 등록된 차량 ID입니다: " + vehicle.getVehicleId());
        }
        if (vehicleRepository.existsByRegistrationNumber(vehicle.getRegistrationNumber())) {
            throw new IllegalArgumentException("이미 등록된 차량 번호입니다: " + vehicle.getRegistrationNumber());
        }
        return vehicleRepository.save(vehicle);
    }

    public Vehicle findVehicleById(String vehicleId) {
        return vehicleRepository.findByVehicleId(vehicleId);
    }
}
