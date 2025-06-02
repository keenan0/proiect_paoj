package org.ticketing_app.contoller;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.ticketing_app.access_code.AccessCode;
import org.ticketing_app.access_code.CustomAccessCode;
import org.ticketing_app.access_code.QrAccessCode;
import org.ticketing_app.dto.TicketingEventDTO;
import org.ticketing_app.model.Marker;
import org.ticketing_app.model.TicketingEvent;
import org.ticketing_app.model.User;
import org.ticketing_app.services.MarkerService;
import org.ticketing_app.services.TicketingEventService;
import org.ticketing_app.user.CustomUserDetails;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final TicketingEventService service;
    private final MarkerService markerService;

    public EventController(TicketingEventService service, MarkerService markerService) {
        this.service = service;
        this.markerService = markerService;
    }

    @GetMapping
    public List<TicketingEvent> getEventsByMarker(@RequestParam Long markerId) {
        List<TicketingEvent> response = service.getEventsByMarker(markerId);

        System.out.println(response);

        return response;
    }

    @PostMapping
    public ResponseEntity<Marker> addEvent(@RequestBody TicketingEventDTO payload) {
        TicketingEvent event = payload.getEvent();
        String accessCodeType = payload.getAccessCode();
        event.setAccessCode(accessCodeType);

        // Get the current user data
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User currentUser = userDetails.getUser();

        event.setUser(currentUser);

        System.out.println(event.getMarker());
        Marker db_saved_updated_marker = markerService.saveOrUpdate(event.getMarker());
        service.saveOrUpdate(event);

        return ResponseEntity.status(HttpStatus.CREATED).body(db_saved_updated_marker);
    }
}
