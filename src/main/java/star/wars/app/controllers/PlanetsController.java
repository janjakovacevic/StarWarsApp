package star.wars.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import star.wars.app.models.planets.Planet;
import star.wars.app.models.planets.Planets;
import star.wars.app.utilities.EndPoints;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PlanetsController {

    @GetMapping("/planets")
    public String viewAllPlanets(RestTemplate restTemplate, ModelMap modelMap,
                                 @RequestParam(value = "name", defaultValue = "") String name) {

        List<Planet> results = new ArrayList<>();
        Planets planets = restTemplate.getForObject(EndPoints.SEARCH_PLANETS + name, Planets.class);

        while(planets.getNext() != null){
            results.addAll(planets.getResults());
            String nextPage = planets.getNext().replace("http", "https");
            planets = restTemplate.getForObject(nextPage, Planets.class);
        }
        results.addAll(planets.getResults());
        modelMap.addAttribute("count", planets.getCount());
        modelMap.addAttribute("planets", results);
        return "/planets";
    }
}
