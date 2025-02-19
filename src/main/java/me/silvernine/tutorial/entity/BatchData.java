package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "data_table")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatchData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date; // 데이터 날짜
    private double value;   // 데이터 값

    public BatchData(LocalDate date, double value) {
        this.date = date;
        this.value = value;
    }
}
