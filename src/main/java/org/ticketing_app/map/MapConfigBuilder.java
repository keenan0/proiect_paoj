package org.ticketing_app.map;

/*
    MapConfigBuilder

    - builder class for MapConfig
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.ticketing_app.model.Marker;
import org.ticketing_app.model.TicketingEvent;
import org.ticketing_app.services.AuditService;

public class MapConfigBuilder {
    private MapConfig mapConfig = new MapConfig();
    private final AuditService auditService;

    public MapConfigBuilder(String mapId, double latitude, double longitude, int zoomLevel, AuditService auditService) {
        this.auditService = auditService;

        mapConfig.setMapId(mapId);
        mapConfig.setLatitude(latitude);
        mapConfig.setLongitude(longitude);
        mapConfig.setZoomLevel(zoomLevel);
    }

    public MapConfigBuilder useTileLayer(String url, String attribution) {
        mapConfig.getTileLayer().setUrl(url);
        mapConfig.getTileLayer().setAttribution(attribution);
        return this;
    }

    //FIX LATER
    public MapConfigBuilder addEvent(String title, String desc, double latitude, double longitude, String popup) {
        Marker marker = new Marker();
        marker.setLatitude(latitude);
        marker.setLongitude(longitude);
        marker.setPopUp(popup);

        TicketingEvent event = new TicketingEvent(title, desc, marker);

        mapConfig.getEvents().add(event);

        return this;
    }

    public MapConfigBuilder addEvent(TicketingEvent event) {
        auditService.logAction("MapConfigBuilder: Added event " + event.toString());
        mapConfig.getEvents().add(event);

        return this;
    }

    public MapConfig build() {
        auditService.logAction("MapConfigBuilder: Built MapConfig.");
        return this.mapConfig;
    }
}
