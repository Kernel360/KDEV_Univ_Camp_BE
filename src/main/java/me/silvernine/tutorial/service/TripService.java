package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.TripRequestDto;
import me.silvernine.tutorial.model.Trip;
import me.silvernine.tutorial.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    // ✅ 단일 저장 (유지)
    public Trip saveTrip(TripRequestDto tripRequestDto) {
        Trip trip = convertToEntity(tripRequestDto);
        return tripRepository.save(trip);
    }

    // ✅ 배치 저장 수정: DTO를 Entity로 변환 후 저장
    public List<Trip> saveTrips(List<Trip> trips) {
        return tripRepository.saveAll(trips);
    }

    // ✅ DTO → Entity 변환 메서드 추가 (단일 및 배치에서 사용)
    public Trip convertToEntity(TripRequestDto dto) {
        Trip trip = new Trip();
        trip.setVehicleId(dto.getVehicleId());
        trip.setLatitude(dto.getLatitude());
        trip.setLongitude(dto.getLongitude());
        trip.setBatteryLevel(dto.getBatteryLevel());
        trip.setTimestamp(dto.getTimestamp()); // ✅ 문자열이 LocalDateTime으로 변환됨
        return trip;
    }
}
