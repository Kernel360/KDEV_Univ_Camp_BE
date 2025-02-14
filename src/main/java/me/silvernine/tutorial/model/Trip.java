package me.silvernine.tutorial.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "trip_data")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleId;
    private Double latitude;
    private Double longitude;
    private int batteryLevel;
    private String timestamp; // ✅ String으로 유지 (단일과 동일)
}
