package me.silvernine.tutorial.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "trip_data")  // ✅ 테이블명이 정확히 맞는지 확인 필요
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String vehicleId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)  // ✅ 배터리 수치 추가
    private Integer batteryLevel;

    @PrePersist
    protected void onCreate() {
        this.timestamp = this.timestamp == null ? LocalDateTime.now() : this.timestamp;  // ✅ 기본값 설정
    }
}
