package me.silvernine.tutorial.controller;

import lombok.RequiredArgsConstructor;  // ✅ 올바른 위치에 유지
import me.silvernine.tutorial.dto.CarRequest;
import me.silvernine.tutorial.dto.CarResponse;
import me.silvernine.tutorial.dto.ControlInfoRequestDto;
import me.silvernine.tutorial.service.CarService;
import me.silvernine.tutorial.service.ControlInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;


@Tag(name = "Car Management", description = "APIs for managing car data and operations")
@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private final ControlInfoService controlInfoService;

    @Operation(summary = "차량 등록", description = "차량 데이터를 관리하는 API로, 차량을 등록하는 기능을 제공합니다")
    @PostMapping
    public ResponseEntity<CarResponse> registerCar(@RequestBody CarRequest request, Principal principal) {
        return ResponseEntity.ok(carService.registerCar(request, principal));
    }

    // 🔥 차량 번호 기반 조회 (404 메시지 추가)
    @Operation(summary = "차량 번호로 조회", description = "차량 번호를 이용해 특정 차량 정보를 조회하는 API입니다.")
    @GetMapping("/number/{vehicleNumber}")
    public ResponseEntity<?> getVehicleByNumber(@PathVariable String vehicleNumber) {
        try {
            return ResponseEntity.ok(carService.getCarByCarNumber(vehicleNumber));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("🚗 차량 번호 '" + vehicleNumber + "'에 해당하는 차량을 찾을 수 없습니다.");
        }
    }

    @Operation(summary = "Get all cars", description = "Fetches a list of all cars in the system")
    @GetMapping
    public ResponseEntity<List<CarResponse>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @Operation(summary = "Send cycle info", description = "Receives and processes cycle information from client")
    @PostMapping("/sendCycleInfo")
    public ResponseEntity<String> sendCycleInfo(@RequestBody ControlInfoRequestDto request) {
        boolean isProcessed = controlInfoService.processCycleInfo(request);
        if (isProcessed) {
            return ResponseEntity.ok("Cycle info processed successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to process cycle info");
        }
    }
}