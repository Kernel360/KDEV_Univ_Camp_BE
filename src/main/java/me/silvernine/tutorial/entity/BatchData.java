package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_data")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatchData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp; // ✅ DATETIME 필드 유지
    private double value;

    public BatchData(LocalDateTime timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }
}
