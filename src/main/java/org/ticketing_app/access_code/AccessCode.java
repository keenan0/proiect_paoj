package org.ticketing_app.access_code;

import org.ticketing_app.model.TicketingEvent;
import org.ticketing_app.model.User;
import java.time.LocalDateTime;

public abstract class AccessCode {
    protected long id;
    protected TicketingEvent event;
    protected String code;
    protected LocalDateTime generatedAt;
    protected User owner;

    public AccessCode() {
        this.generatedAt = LocalDateTime.now();
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public abstract void generate();

    @Override
    public String toString() {
        return "AccessCode{" +
                "code='" + code + '\'' +
                ", generatedAt=" + generatedAt +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TicketingEvent getEvent() {
        return event;
    }

    public void setEvent(TicketingEvent event) {
        this.event = event;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
