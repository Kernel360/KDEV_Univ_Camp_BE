package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "trip_data")
public class TripData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private LocalDate date;
    private LocalTime time;
    private String vehicleId;
    private double latitude;
    private double longitude;

    @Column(name = "created_at", updatable = false, insertable = false)
    private String createdAt;
}
