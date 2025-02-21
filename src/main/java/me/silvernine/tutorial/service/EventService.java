package me.silvernine.tutorial.service;

import me.silvernine.tutorial.dto.VehicleEventRequest;
import me.silvernine.tutorial.entity.VehicleEvent;
import me.silvernine.tutorial.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public void saveEvent(VehicleEventRequest request) {
        VehicleEvent event = new VehicleEvent();
        event.setVehicleId(request.getVehicleId());
        event.setEventType(request.getEventType());
        event.setTimestamp(request.getTimestamp());
        eventRepository.save(event);
    }
}