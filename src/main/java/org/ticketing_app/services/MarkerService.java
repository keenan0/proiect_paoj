package org.ticketing_app.services;

import org.springframework.stereotype.Service;
import org.ticketing_app.model.Marker;
import org.ticketing_app.model.TicketingEvent;
import org.ticketing_app.repositories.MarkerRepository;
import org.ticketing_app.repositories.TicketingEventRepository;

import java.util.List;

@Service
public class MarkerService {
    private final MarkerRepository markerRepo;

    public MarkerService(MarkerRepository markerRepo) {
        this.markerRepo = markerRepo;
    }

    public Marker saveOrUpdate(Marker marker) {
        return markerRepo.save(marker);
    }
}
