package me.silvernine.tutorial.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "car")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "car_name", nullable = false)
    private String carName; // 차량 이름

    @Column(name = "car_number", nullable = false, unique = true)
    private String carNumber; // 차량 번호

    @Column(name = "owner_username", nullable = false)
    private String ownerUsername; // 차량 소유자 (사용자 이름)
}
