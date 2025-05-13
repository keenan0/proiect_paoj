package org.ticketing_app.model;

/*
    TileLayer

    - part of MapConfig
    - each map has a tileLayer used to render the map

    TODO:
        DONE in MapController: add enums for different tileLayers
 */

import org.springframework.stereotype.Component;

@Component
public class TileLayer {
    private String url = "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png";
    private String attribution = "&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors";

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public String getAttribution() {
        return attribution;
    }
    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }
}
