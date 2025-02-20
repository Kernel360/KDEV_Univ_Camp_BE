package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_table")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatchData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;
    private double value;

    public BatchData(LocalDateTime timestamp, double value) {
        this.timestamp = timestamp.withNano(0); // ✅ 나노초 제거
        this.value = value;
    }
}
