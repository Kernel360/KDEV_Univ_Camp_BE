package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
        import lombok.*;

        import java.time.LocalDate;

@Entity
@Table(name = "statistics_table")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatchStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;  // 통계 대상 날짜
    private long count;      // 데이터 개수
    private double avgValue; // 평균 값

    public BatchStatistics(LocalDate date, long count, double avgValue) {
        this.date = date;
        this.count = count;
        this.avgValue = avgValue;
    }
}
