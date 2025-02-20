package me.silvernine.tutorial.controller;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.entity.Vehicle;
import me.silvernine.tutorial.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicle")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    // ✅ 차량 등록 (중복 방지 및 vehicleId 자동 설정 추가)
    @PostMapping
    public ResponseEntity<?> registerVehicle(@RequestBody Vehicle vehicle) {
        try {
            // ✅ vehicleId가 없으면 registrationNumber를 vehicleId로 설정
            if (vehicle.getVehicleId() == null || vehicle.getVehicleId().isBlank()) {
                vehicle.setVehicleId(vehicle.getRegistrationNumber());
            }

            Vehicle savedVehicle = vehicleService.saveVehicle(vehicle);
            return ResponseEntity.ok(savedVehicle);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // ✅ 중복 발생 시 예외 메시지 반환
        }
    }

    // ✅ 특정 차량 조회 기능 추가
    @GetMapping("/{vehicleId}")
    public ResponseEntity<?> getVehicle(@PathVariable String vehicleId) {
        Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
        if (vehicle == null) {
            return ResponseEntity.badRequest().body("해당 차량을 찾을 수 없습니다: " + vehicleId);
        }
        return ResponseEntity.ok(vehicle);
    }
}
