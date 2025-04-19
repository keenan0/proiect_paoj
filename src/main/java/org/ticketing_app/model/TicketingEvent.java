package org.ticketing_app.model;

public class TicketingEvent {
    private String title;
    private String description;
    private Marker marker;

    public TicketingEvent() {}

    public TicketingEvent(String title, String description, Marker marker) {
        this.title = title;
        this.description = description;
        this.marker = marker;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Marker getMarker() { return marker; }
    public void setMarker(Marker marker) { this.marker = marker; }

    @Override
    public String toString() {
        return "TicketingEvent{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", marker=" + marker +
                '}';
    }
}
