package org.ticketing_app.map;
import org.ticketing_app.model.Marker;
import org.ticketing_app.model.TicketingEvent;
import org.ticketing_app.model.TileLayer;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LeafletMapService {
    public String generateMapDiv(MapConfig config) {
        return String.format(
                "<div class=\"map\" id=\"%s\"></div>",
                config.getMapId()
        );
    }

    public String generateMapScript(MapConfig config) {
        StringBuilder js = new StringBuilder();

        String mapName = "leaf" + config.getMapId();

        js.append(String.format(
                Locale.US,
                "var %s = L.map('%s').setView([%.6f, %.6f], %d);\nwindow['%s'] = %s;\n",
                mapName, config.getMapId(),
                config.getLatitude(), config.getLongitude(),
                config.getZoomLevel(),
                mapName, mapName
        ));

        TileLayer tileLayer = config.getTileLayer();
        js.append(String.format(
                Locale.US,
                "L.tileLayer('%s', {attribution: '%s'}).addTo(%s);\n",
                tileLayer.getUrl(), tileLayer.getAttribution(), mapName
        ));

        for (TicketingEvent event : config.getEvents()) {
            Marker marker = event.getMarker();

            js.append(String.format(
                    Locale.US,
                    "L.marker([%.6f, %.6f]).addTo(%s).bindPopup('%s');\n",
                    marker.getLatitude(), marker.getLongitude(),
                    mapName,
                    marker.getPopUp()
            ));
        }

        js.append(String.format(
                Locale.US,
                "document.dispatchEvent(new CustomEvent('mapReady', { detail: 'leaf%s' }));\nconsole.log('Initialised leaflet map.');\n",
                config.getMapId()
        ));

        return js.toString();
    }
}
