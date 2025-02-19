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

    private LocalDateTime timestamp;  // ✅ LocalDateTime으로 저장
    private long count;
    private double avgValue;

    public BatchStatistics(LocalDateTime timestamp, long count, double avgValue) {
        this.timestamp = timestamp;
        this.count = count;
        this.avgValue = avgValue;
    }
}
