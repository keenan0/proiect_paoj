package org.ticketing_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ticketing_app.model.TicketingEvent;
import org.ticketing_app.repositories.TicketingEventRepository;

import java.util.List;

@Service
public class TicketingEventService {

    private final TicketingEventRepository eventRepository;

    @Autowired
    private AuditService auditService;

    public TicketingEventService(TicketingEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<TicketingEvent> getAllEvents() {
        auditService.logAction("TicketingEventService: Get Events");
        return eventRepository.findAll();
    }

    public List<TicketingEvent> getEventsByMarker(Long markerId) {
        auditService.logAction("TicketingEventService: Get Events with marker = " + markerId);
        return eventRepository.findByMarkerId(markerId);
    }

    public TicketingEvent saveOrUpdate(TicketingEvent event) {
        auditService.logAction("TicketingEventService: Save/Update event");
        return eventRepository.save(event);
    }
}
