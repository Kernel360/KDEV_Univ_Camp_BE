package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicle")
@Getter
@Setter
@NoArgsConstructor
public class Vehicle {

    @Id
    @Column(name = "vehicle_id", length = 50, nullable = false, unique = true)  // ✅ NULL 방지 & 유니크 설정
    private String vehicleId;

    private String model;

    @Column(name = "registration_number", unique = true)
    private String registrationNumber;

    private String owner;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ✅ vehicle_id 자동 생성
    @PrePersist
    public void generateIdIfAbsent() {
        if (this.vehicleId == null || this.vehicleId.isBlank()) {
            this.vehicleId = UUID.randomUUID().toString();
        }
    }
}
