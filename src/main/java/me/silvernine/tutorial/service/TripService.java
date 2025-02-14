package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.TripRequestDto;
import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.repository.TripRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {
    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    // ✅ 단일 데이터 저장
    public Trip saveTrip(TripRequestDto tripRequestDto) {
        Trip trip = new Trip();
        trip.setVehicleId(tripRequestDto.getVehicleId());
        trip.setLatitude(tripRequestDto.getLatitude());
        trip.setLongitude(tripRequestDto.getLongitude());
        trip.setBatteryLevel(tripRequestDto.getBatteryLevel());
        trip.setTimestamp(tripRequestDto.getTimestamp()); // ✅ timestamp 변환 적용

        return tripRepository.save(trip);
    }

    // ✅ 배치 데이터 저장 수정
    public List<Trip> saveTrips(List<TripRequestDto> tripRequestDtoList) {
        List<Trip> trips = tripRequestDtoList.stream().map(dto -> {
            Trip trip = new Trip();
            trip.setVehicleId(dto.getVehicleId());
            trip.setLatitude(dto.getLatitude());
            trip.setLongitude(dto.getLongitude());
            trip.setBatteryLevel(dto.getBatteryLevel());
            trip.setTimestamp(dto.getTimestamp()); // ✅ timestamp 변환 적용
            return trip;
        }).collect(Collectors.toList());

        return tripRepository.saveAll(trips);
    }
}
