package org.ticketing_app.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.ticketing_app.map.LeafletMapService;
import org.ticketing_app.map.MapConfig;
import org.ticketing_app.map.MapConfigBuilder;

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

    public MapController(LeafletMapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/")
    public String index(Model model) {
        System.out.println("Hit index controller");

        MapConfig mapConfig = new MapConfigBuilder( "map",  44.434891, 26.080374, 20)
                .useTileLayer(TILE_LAYER_TYPE.VOYAGER.getUrl(),
                        TILE_LAYER_TYPE.VOYAGER.getAttribution())
                .addEvent("","",44.435726, 26.07958, "Opera Nationala")
                .addEvent("","",44.434929, 26.079547, "Parcul Operei")
                .build();

        System.out.println("Hit index controller!");

        model.addAttribute("mapId", mapConfig.getMapId());
        model.addAttribute("mapDiv", mapService.generateMapDiv(mapConfig));
        model.addAttribute("mapScript", mapService.generateMapScript(mapConfig));

        return "index";
    }
}
