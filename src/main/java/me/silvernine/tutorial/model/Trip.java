package me.silvernine.tutorial.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleId;
    private double latitude;
    private double longitude;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp;
}