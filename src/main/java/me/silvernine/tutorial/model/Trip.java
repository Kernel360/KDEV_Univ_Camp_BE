package me.silvernine.tutorial.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.silvernine.tutorial.util.LocalDateTimeAttributeConverter;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_data")
@Getter
@Setter
@NoArgsConstructor
public class TripData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vehicleId;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Integer batteryLevel;

    @Column(nullable = false)
    @Convert(converter = LocalDateTimeAttributeConverter.class)  // LocalDateTime 변환 추가
    private LocalDateTime timestamp;
}
