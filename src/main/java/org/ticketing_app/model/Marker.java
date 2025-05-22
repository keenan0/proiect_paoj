package org.ticketing_app.model;

/*
    Marker

    - part of MapConfig
    - contains data for rendering a marker on the map
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class Marker {
    private long id;
    private double latitude;
    private double longitude;
    private String popUp;

    public Marker() {
        this.id = 0;
        this.latitude = 0;
        this.longitude = 0;
        this.popUp = "";
    }

    public Marker(long id, double latitude, double longitude, String popUp) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.popUp = popUp;
    }

    public String getPopUp() {
        return popUp;
    }

    public void setPopUp(String popUp) {
        this.popUp = popUp;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Marker{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", popUp='" + popUp + '\'' +
                '}';
    }
}
