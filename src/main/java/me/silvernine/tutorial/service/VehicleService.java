package me.silvernine.tutorial.service;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.entity.Vehicle;
import me.silvernine.tutorial.entity.Car;
import me.silvernine.tutorial.repository.VehicleRepository;
import me.silvernine.tutorial.repository.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final CarRepository carRepository;  // ✅ cars 테이블과 연동

    @Transactional
    public Vehicle saveVehicle(Vehicle vehicle) {
        // ✅ vehicleId가 없으면 UUID 자동 생성
        if (vehicle.getVehicleId() == null || vehicle.getVehicleId().isBlank()) {
            vehicle.setVehicleId(UUID.randomUUID().toString());
        }

        // ✅ 중복 등록 방지
        if (vehicleRepository.existsByVehicleId(vehicle.getVehicleId())) {
            throw new IllegalArgumentException("이미 등록된 차량 ID입니다: " + vehicle.getVehicleId());
        }
        if (vehicleRepository.existsByRegistrationNumber(vehicle.getRegistrationNumber())) {
            throw new IllegalArgumentException("이미 등록된 차량 번호입니다: " + vehicle.getRegistrationNumber());
        }

        // ✅ 차량 정보 저장 (vehicle 테이블)
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        // ✅ cars 테이블에 vehicle_id 추가 (등록번호 기준 매칭)
        Optional<Car> carOptional = carRepository.findByCarNumber(vehicle.getRegistrationNumber());
        carOptional.ifPresent(car -> {
            car.setVehicleId(savedVehicle.getVehicleId()); // ✅ vehicle_id 설정
            carRepository.save(car); // ✅ cars 테이블에 업데이트
        });

        return savedVehicle;
    }

    public Vehicle findVehicleById(String vehicleId) {
        return vehicleRepository.findByVehicleId(vehicleId);
    }
}
