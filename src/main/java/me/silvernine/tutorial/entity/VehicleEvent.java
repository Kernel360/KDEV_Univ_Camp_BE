package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_event")
@Data
public class VehicleEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleId;
    private String eventType; // "START" or "STOP"
    private LocalDateTime timestamp;
}