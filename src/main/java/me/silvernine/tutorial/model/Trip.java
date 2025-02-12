package me.silvernine.tutorial.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trip_data")  // DB 테이블명
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vehicleId;
    private String timestamp;  // 기존 date + time 제거 후 timestamp 추가
    private Double latitude;
    private Double longitude;
}
