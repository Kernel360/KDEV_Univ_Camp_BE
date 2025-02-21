package me.silvernine.tutorial.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleEventRequest {
    private String vehicleId;
    private String eventType; // "START" or "STOP"
    private LocalDateTime timestamp;
}
