package org.ticketing_app.model;

/*
    Marker

    - part of MapConfig
    - contains data for rendering a marker on the map
 */

public class Marker {
    private double latitude;
    private double longitude;
    private String popUp;

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

    @Override
    public String toString() {
        return "Marker{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", popUp='" + popUp + '\'' +
                '}';
    }
}
