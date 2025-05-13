package org.ticketing_app.dto;

import org.ticketing_app.model.TicketingEvent;

public class TicketingEventDTO {
    private TicketingEvent event;
    private String accessCode;

    public TicketingEvent getEvent() {
        return event;
    }

    public void setEvent(TicketingEvent event) {
        this.event = event;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
}
