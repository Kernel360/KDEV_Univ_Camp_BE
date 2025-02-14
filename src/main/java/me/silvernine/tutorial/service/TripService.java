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

    public Trip saveTrip(TripRequestDto tripRequestDto) {
        Trip trip = new Trip();
        trip.setVehicleId(tripRequestDto.getVehicleId());
        trip.setLatitude(tripRequestDto.getLatitude());
        trip.setLongitude(tripRequestDto.getLongitude());
        trip.setTimestamp(tripRequestDto.getTimestamp());

        return tripRepository.save(trip);
    }

    public List<Trip> saveTrips(List<TripRequestDto> tripRequestDtoList) {
        List<Trip> trips = tripRequestDtoList.stream().map(dto -> {
            Trip trip = new Trip();
            trip.setVehicleId(dto.getVehicleId());
            trip.setLatitude(dto.getLatitude());
            trip.setLongitude(dto.getLongitude());
            trip.setTimestamp(dto.getTimestamp());
            return trip;
        }).collect(Collectors.toList());

        return tripRepository.saveAll(trips);
    }
}

