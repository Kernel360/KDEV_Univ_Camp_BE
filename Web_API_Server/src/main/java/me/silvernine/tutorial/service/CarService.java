package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.*;
import me.silvernine.tutorial.entity.Car;
import me.silvernine.tutorial.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
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

    // 사용자 차량 목록 조회
    public List<CarResponse> getUserCars(Principal principal) {
        return carRepository.findByOwnerUsername(principal.getName()).stream()
                .map(car -> CarResponse.builder()
                        .id(car.getId())
                        .carName(car.getCarName())
                        .carNumber(car.getCarNumber())
                        .ownerUsername(car.getOwnerUsername())
                        .build())
                .collect(Collectors.toList());
    }

    // 차량 단건 조회
    public CarResponse getCarById(Long id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
        return CarResponse.builder()
                .id(car.getId())
                .carName(car.getCarName())
                .carNumber(car.getCarNumber())
                .ownerUsername(car.getOwnerUsername())
                .build();
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


    // 주기 정보 전달에 따른 응답
    public ResponseDto processCycleInfo(CarCycleInfoRequest request) {
        // 실패 처리 코드
        if (request.getMdn() == null) {
            return ResponseDto.builder()
                    .rstCd("444")
                    .rstMsg("Failure")
                    .build();
        }

        // 성공 처리 코드
        return ResponseDto.builder()
                .rstCd("000")
                .rstMsg("Success")
                .mdn(request.getMdn())
                .build();
    }

    // 시동 on 정보 전달에 따른 응답
    public ResponseDto processCarStart(CarStartupRequest request) {

        // 성공 처리 코드
        return ResponseDto.builder()
                .rstCd("000")
                .rstMsg("Success")
                .mdn(request.getMdn())
                .build();
    }

    // 시동 off 정보 전달에 따른 응답
    public ResponseDto processCarStop(CarStartupRequest request) {

        // 성공 처리 코드
        return ResponseDto.builder()
                .rstCd("000")
                .rstMsg("Success")
                .mdn(request.getMdn())
                .build();
    }

    // 시동 off 정보 전달에 따른 응답
    public ResponseDto processGeoPoint(CarGeoPointRequest request) {

        // 성공 처리 코드
        return ResponseDto.builder()
                .rstCd("000")
                .rstMsg("Success")
                .mdn(request.getMdn())
                .build();
    }
}
