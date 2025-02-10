package me.silvernine.tutorial.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trip_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String date;
    private String time;
    private String vehicleId;
    private Double latitude;
    private Double longitude;
}
