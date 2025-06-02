package org.ticketing_app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.ticketing_app.access_code.AccessCode;
import org.ticketing_app.access_code.AccessCodeFactory;
import org.ticketing_app.access_code.CustomAccessCode;
import org.ticketing_app.access_code.QrAccessCode;
import org.ticketing_app.model.TicketingEvent;
import org.ticketing_app.model.User;
import org.ticketing_app.repositories.AccessCodeRepository;
import org.ticketing_app.repositories.TicketingEventRepository;
import org.ticketing_app.user.CustomUserRepository;

@Service
public class AccessCodeService {

    private final AccessCodeFactory accessCodeFactory;
    private final AccessCodeRepository accessCodeRepository;
    private final CustomUserRepository userRepository;
    private final TicketingEventRepository eventRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    public AccessCodeService(
            AccessCodeFactory accessCodeFactory,
            AccessCodeRepository accessCodeRepository,
            CustomUserRepository userRepository,
            TicketingEventRepository eventRepository
    ) {
        this.accessCodeFactory = accessCodeFactory;
        this.accessCodeRepository = accessCodeRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    public AccessCode createAccessCode(String type, Long userId, Long eventId) {
        AccessCode accessCode = accessCodeFactory.createAccessCode(type);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        TicketingEvent event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        accessCode.setOwner(user);
        accessCode.setEvent(event);
        accessCode.generate();

        auditService.logAction("AccessCodeService: Created new access code");

        return accessCodeRepository.save(accessCode);
    }
}
