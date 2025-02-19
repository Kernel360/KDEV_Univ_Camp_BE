package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "statistics_table")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatchStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;  // ✅ LocalDate → LocalDateTime 변경
    private long count;      // 데이터 개수
    private double avgValue; // 평균 값

    public BatchStatistics(LocalDateTime timestamp, long count, double avgValue) {
        this.timestamp = timestamp;
        this.count = count;
        this.avgValue = avgValue;
    }
}
