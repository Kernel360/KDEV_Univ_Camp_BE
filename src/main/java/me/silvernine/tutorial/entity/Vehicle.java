package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {
    @Id
    @Column(name = "vehicle_id", length = 50, nullable = false, unique = true) // ✅ 차량 ID는 기본 키로 사용됨
    private String vehicleId;

    @Column(nullable = false) // ✅ NULL 방지
    private String model;

    @Column(name = "registration_number", nullable = false, unique = true) // ✅ 중복 방지
    private String registrationNumber;

    @Column(nullable = false)
    private String owner;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
