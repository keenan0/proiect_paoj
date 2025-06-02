package org.ticketing_app.model;

import org.springframework.stereotype.Component;
import org.ticketing_app.access_code.AccessCode;

@Component
public class TicketingEvent {
    private long id;
    private String title;
    private String description;

    private Marker marker;
    private User user;

    private String accessCode;

    public TicketingEvent() {}

    public TicketingEvent(String title, String description, Marker marker) {
        // This event won't require an access code
        this.title = title;
        this.description = description;
        this.marker = marker;

        this.accessCode = null;
        this.user = null;
    }

    public TicketingEvent(String title, String description, Marker marker, String accessCode) {
        // Constructor with access code
        this.title = title;
        this.description = description;
        this.marker = marker;

        this.accessCode = accessCode;
        this.user = null;
    }

    public TicketingEvent(String title, String description, Marker marker, String accessCode, User user) {
        // Constructor with access code
        this.title = title;
        this.description = description;
        this.marker = marker;

        this.accessCode = accessCode;
        this.user = user;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Marker getMarker() { return marker; }
    public void setMarker(Marker marker) { this.marker = marker; }

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }
    public String getAccessCode() { return accessCode; }

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
