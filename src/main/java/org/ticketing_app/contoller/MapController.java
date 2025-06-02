package org.ticketing_app.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.ticketing_app.map.LeafletMapService;
import org.ticketing_app.map.MapConfig;
import org.ticketing_app.map.MapConfigBuilder;
import org.ticketing_app.model.TicketingEvent;
import org.ticketing_app.services.AuditService;
import org.ticketing_app.services.TicketingEventService;

import java.util.List;

enum TILE_LAYER_TYPE {
    DEFAULT(
            "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
            "Map data © OpenStreetMap contributors"
    ),
    VOYAGER(
            "https://{s}.basemaps.cartocdn.com/rastertiles/voyager_labels_under/{z}/{x}/{y}{r}.png",
            "&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors " +
                    "&copy; <a href=\"https://carto.com/attributions\">CARTO</a>"
    );

    private final String url;
    private final String attribution;

    TILE_LAYER_TYPE(String url, String attribution) {
        this.url = url;
        this.attribution = attribution;
    }

    public String getUrl() { return url; }

    public String getAttribution() { return attribution; }
}

@Controller
public class MapController {
    private final LeafletMapService mapService;
    private final TicketingEventService eventService;

    @Autowired
    private AuditService auditService;

    public MapController(LeafletMapService mapService, TicketingEventService eventService) {
        this.mapService = mapService;
        this.eventService = eventService;
    }

    @GetMapping("/")
    public String index(Model model) {
        System.out.println("Hit index controller");

        MapConfigBuilder mapBuilder = new MapConfigBuilder("map", 44.434891, 26.080374, 20, auditService)
                .useTileLayer(TILE_LAYER_TYPE.VOYAGER.getUrl(), TILE_LAYER_TYPE.VOYAGER.getAttribution());

        // Fetch all events from DB
        List<TicketingEvent> events = eventService.getAllEvents();

        // Add each event to the map
        for (TicketingEvent event : events) {
            mapBuilder.addEvent(event);
        }

        MapConfig mapConfig = mapBuilder.build();

        model.addAttribute("mapId", mapConfig.getMapId());
        model.addAttribute("mapDiv", mapService.generateMapDiv(mapConfig));
        model.addAttribute("mapScript", mapService.generateMapScript(mapConfig));

        return "index";
    }
}
