package me.silvernine.tutorial.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRequestDto {
    private String vehicleId;
    private String timestamp;  // "2024-11-30 00:01:20.00"
    private Double latitude;
    private Double longitude;
}
