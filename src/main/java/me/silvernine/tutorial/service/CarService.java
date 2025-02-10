package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.CarRequest;
import me.silvernine.tutorial.dto.CarResponse;
import me.silvernine.tutorial.entity.Car;
import me.silvernine.tutorial.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    // 차량 등록
    public CarResponse registerCar(CarRequest request, Principal principal) {
        Car car = Car.builder()
                .carName(request.getCarName())
                .carNumber(request.getCarNumber())
                .ownerUsername(principal.getName())
                .build();
        Car savedCar = carRepository.save(car);

        return CarResponse.builder()
                .id(savedCar.getId())
                .carName(savedCar.getCarName())
                .carNumber(savedCar.getCarNumber())
                .ownerUsername(savedCar.getOwnerUsername())
                .build();
    }

    // 차량 번호로 조회
    public Object getCarByCarNumber(String carNumber) {
        Optional<Car> carOptional = carRepository.findByCarNumber(carNumber);
        if (carOptional.isPresent()) {
            Car car = carOptional.get();
            return CarResponse.builder()
                    .id(car.getId())
                    .carName(car.getCarName())
                    .carNumber(car.getCarNumber())
                    .ownerUsername(car.getOwnerUsername())
                    .build();
        } else {
            return Collections.singletonMap("message", "🚗 차량 번호 '" + carNumber + "'에 해당하는 차량을 찾을 수 없습니다.");
        }
    }

    // 차량 전체 조회
    public List<CarResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(car -> CarResponse.builder()
                        .id(car.getId())
                        .carName(car.getCarName())
                        .carNumber(car.getCarNumber())
                        .ownerUsername(car.getOwnerUsername())
                        .build())
                .collect(Collectors.toList());
    }
}
