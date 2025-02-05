package me.silvernine.tutorial.service;

import me.silvernine.tutorial.entity.TripData;
import me.silvernine.tutorial.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public void saveTripData(List<TripData> tripDataList) {
        tripRepository.saveAll(tripDataList);
    }
}
