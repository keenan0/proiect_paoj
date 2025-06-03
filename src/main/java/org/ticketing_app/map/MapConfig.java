package org.ticketing_app.map;

import org.ticketing_app.model.Marker;
import org.ticketing_app.model.TicketingEvent;
import org.ticketing_app.model.TileLayer;

import java.util.List;
import java.util.ArrayList;

public class MapConfig {
    private String mapId;
    private double latitude;
    private double longitude;
    private int zoomLevel;

    private List<TicketingEvent> events = new ArrayList<>();
    private TileLayer tileLayer = new TileLayer();

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public List<TicketingEvent> getEvents() {
        return events;
    }

    public void setEvents(List<TicketingEvent> events) {
        this.events = events;
    }

    public TileLayer getTileLayer() {
        return tileLayer;
    }

    public void setTileLayer(TileLayer tileLayer) {
        this.tileLayer = tileLayer;
    }

    @Override
    public String toString() {
        return "MapConfig{" +
                "events=" + events +
                ", mapId='" + mapId + '\'' +
                '}';
    }
}
