package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "data_table")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatchData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp; // ✅ LocalDate → LocalDateTime으로 변경
    private double value; // 데이터 값

    public BatchData(LocalDateTime timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }
}
