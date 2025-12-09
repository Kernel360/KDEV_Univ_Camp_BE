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
    private String carName;

    @Column(name = "car_number", nullable = false, unique = true)
    private String carNumber;

    @Column(name = "owner_username", nullable = false)
    private String ownerUsername;
}
