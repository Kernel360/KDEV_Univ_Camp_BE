package me.silvernine.tutorial.controller;

import me.silvernine.tutorial.dto.VehicleEventRequest;
import me.silvernine.tutorial.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<String> saveEvent(@RequestBody VehicleEventRequest eventRequest) {
        eventService.saveEvent(eventRequest);
        return ResponseEntity.ok("이벤트 저장 완료");
    }
}
