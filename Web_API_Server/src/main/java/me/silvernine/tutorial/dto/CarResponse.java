package me.silvernine.tutorial.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CarResponse {
    private Long id;
    private String carName;
    private String carNumber;
    private String ownerUsername;
}