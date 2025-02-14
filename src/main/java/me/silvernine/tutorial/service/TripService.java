package me.silvernine.tutorial.service;

import lombok.RequiredArgsConstructor;
import me.silvernine.tutorial.model.TripData;
import me.silvernine.tutorial.repository.TripRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    public TripData saveTrip(TripData tripData) {
        // String -> LocalDateTime 변환 후 저장
        tripData.setTimestamp(LocalDateTime.parse(tripData.getTimestamp().toString(), formatter));
        return tripRepository.save(tripData);
    }

    public void saveTrips(List<TripData> tripDataList) {
        for (TripData trip : tripDataList) {
            trip.setTimestamp(LocalDateTime.parse(trip.getTimestamp().toString(), formatter));
        }
        tripRepository.saveAll(tripDataList);
    }

    public List<TripData> getRecentTrips(LocalDateTime since) {
        return tripRepository.findByTimestampAfter(since);
    }
}
