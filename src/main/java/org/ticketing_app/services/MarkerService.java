package org.ticketing_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ticketing_app.model.Marker;
import org.ticketing_app.model.TicketingEvent;
import org.ticketing_app.repositories.MarkerRepository;
import org.ticketing_app.repositories.TicketingEventRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MarkerService {
    private final MarkerRepository markerRepo;

    @Autowired
    private AuditService auditService;

    public MarkerService(MarkerRepository markerRepo) {
        this.markerRepo = markerRepo;
    }

    public Marker saveOrUpdate(Marker marker) {
        if (marker.getId() != 0) {
            System.out.println("Salvat marker cu id " + marker.getId());
            auditService.logAction("MarkerService: Saved marker " + marker.getId());
            return markerRepo.save(marker);
        }

        Optional<Marker> existing = markerRepo.findByLatitudeAndLongitude(
                marker.getLatitude(), marker.getLongitude()
        );

        if (existing.isPresent()) {
            System.out.println("Markerul exista deja");
            auditService.logAction("MarkerService: Marker exists");
            return existing.get();
        }

        System.out.println("Am adaugat marker nou");
        auditService.logAction("MarkerService: Added marker");
        return markerRepo.save(marker);
    }

    public Marker findById(Long markerId) {
        return this.markerRepo.findById(markerId);
    }
}
