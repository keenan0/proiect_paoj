package org.ticketing_app.model;

import org.springframework.stereotype.Component;
import org.ticketing_app.access_code.AccessCode;

@Component
public class TicketingEvent {
    private String title;
    private String description;
    private Marker marker;

    private AccessCode accessCode;

    public TicketingEvent() {}

    public TicketingEvent(String title, String description, Marker marker) {
        // This event won't require an access code
        this.title = title;
        this.description = description;
        this.marker = marker;

        this.accessCode = null;
    }

    public TicketingEvent(String title, String description, Marker marker, AccessCode accessCode) {
        // Constructor with access code
        this.title = title;
        this.description = description;
        this.marker = marker;

        this.accessCode = accessCode;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Marker getMarker() { return marker; }
    public void setMarker(Marker marker) { this.marker = marker; }

    public void setAccessCode(AccessCode accessCode) { this.accessCode = accessCode; }
    public AccessCode getAccessCode() { return accessCode; }

    @Override
    public String toString() {
        return "TicketingEvent{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", marker=" + marker +
                ", accessCode=" + accessCode +
                '}';
    }
}
