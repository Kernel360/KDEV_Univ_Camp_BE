package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.dto.CarRequest;
import me.silvernine.tutorial.dto.CarResponse;
import me.silvernine.tutorial.dto.ControlInfoRequestDto;
import me.silvernine.tutorial.service.CarService;
import me.silvernine.tutorial.service.ControlInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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

    @Operation(summary = "차량 전체 조회", description = "차량 데이터를 관리하는 API로, 차량을 조회하는 기능을 제공합니다")
    @GetMapping("/user")
    public ResponseEntity<List<CarResponse>> getUserCars(Principal principal) {
        return ResponseEntity.ok(carService.getUserCars(principal));
    }

    @Operation(summary = "차량 단건 조회", description = "차량 데이터를 관리하는 API로, 차량 ID를 사용하여 차량을 조회하는 기능을 제공합니다")
    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
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
