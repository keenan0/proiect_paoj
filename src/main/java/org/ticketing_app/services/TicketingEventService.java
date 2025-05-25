package org.ticketing_app.services;

import org.springframework.stereotype.Service;
import org.ticketing_app.model.TicketingEvent;
import org.ticketing_app.repositories.TicketingEventRepository;

import java.util.List;

@Service
public class TicketingEventService {

    private final TicketingEventRepository eventRepository;

    public TicketingEventService(TicketingEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<TicketingEvent> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<TicketingEvent> getEventsByMarker(Long markerId) {
        return eventRepository.findByMarkerId(markerId);
    }

    public TicketingEvent saveOrUpdate(TicketingEvent event) {
        return eventRepository.save(event);
    }
}
