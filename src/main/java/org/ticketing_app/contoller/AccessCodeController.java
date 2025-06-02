package org.ticketing_app.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ticketing_app.access_code.AccessCode;
import org.ticketing_app.dto.TicketingEventDTO;
import org.ticketing_app.model.User;
import org.ticketing_app.services.AccessCodeService;
import org.ticketing_app.user.CustomUserDetails;

@RestController
@RequestMapping("/api/access-code")
public class AccessCodeController {
    private final AccessCodeService accessCodeService;

    public AccessCodeController(AccessCodeService accessCodeService) {
        this.accessCodeService = accessCodeService;
    }

    @PostMapping
    public ResponseEntity<?> createAccessCode(@RequestBody TicketingEventDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        User currentUser = userDetails.getUser();

        Long userId = currentUser.getId();

        AccessCode code = accessCodeService.createAccessCode(request.getAccessCode(), userId, request.getEvent().getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(code);
    }
}
