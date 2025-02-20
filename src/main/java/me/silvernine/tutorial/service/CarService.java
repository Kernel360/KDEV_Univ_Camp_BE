package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.CarRequest;
import me.silvernine.tutorial.dto.CarResponse;
import me.silvernine.tutorial.entity.Car;
import me.silvernine.tutorial.entity.Vehicle;
import me.silvernine.tutorial.repository.CarRepository;
import me.silvernine.tutorial.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final VehicleRepository vehicleRepository; // ✅ VehicleRepository 추가

    // ✅ 차량 등록 (Vehicle과 연동)
    public CarResponse registerCar(CarRequest request, Principal principal) {
        // 🚗 차량 정보 생성
        Car car = Car.builder()
                .carName(request.getCarName())
                .carNumber(request.getCarNumber())
                .ownerUsername(principal.getName())
                .build();

        // ✅ Vehicle 테이블에서 차량 번호로 조회
        Optional<Vehicle> vehicleOptional = vehicleRepository.findByRegistrationNumber(car.getCarNumber());

        // 🚗 Vehicle ID가 존재하면 설정
        vehicleOptional.ifPresent(vehicle -> car.setVehicleId(vehicle.getVehicleId()));

        // 🚀 차량 저장
        Car savedCar = carRepository.save(car);

        return CarResponse.builder()
                .id(savedCar.getId())
                .carName(savedCar.getCarName())
                .carNumber(savedCar.getCarNumber())
                .ownerUsername(savedCar.getOwnerUsername())
                .vehicleId(savedCar.getVehicleId()) // ✅ Vehicle ID 응답 포함
                .build();
    }

    // ✅ 차량 번호로 조회
    public CarResponse getCarByCarNumber(String carNumber) {
        Optional<Car> carOptional = carRepository.findByCarNumber(carNumber);
        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            return CarResponse.builder()
                    .id(car.getId())
                    .carName(car.getCarName())
                    .carNumber(car.getCarNumber())
                    .ownerUsername(car.getOwnerUsername())
                    .vehicleId(car.getVehicleId()) // ✅ Vehicle ID 포함
                    .build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found with number: " + carNumber);
        }
    }

    // ✅ 차량 전체 조회
    public List<CarResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(car -> CarResponse.builder()
                        .id(car.getId())
                        .carName(car.getCarName())
                        .carNumber(car.getCarNumber())
                        .ownerUsername(car.getOwnerUsername())
                        .vehicleId(car.getVehicleId()) // ✅ Vehicle ID 포함
                        .build())
                .collect(Collectors.toList());
    }
}
