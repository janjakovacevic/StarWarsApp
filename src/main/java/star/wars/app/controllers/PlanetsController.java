package star.wars.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import star.wars.app.models.films.Film;
import star.wars.app.models.people.Person;
import star.wars.app.models.planets.Planet;
import star.wars.app.models.planets.Planets;
import star.wars.app.models.vehicles.Vehicle;
import star.wars.app.utilities.EndPoints;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PlanetsController {

    @GetMapping("/planets/details/{name}")
    public String viewPlanet(RestTemplate restTemplate, ModelMap modelMap,
                                 @PathVariable String name) {

        List<Planet> results = new ArrayList<>();
        Planets planets = restTemplate.getForObject(EndPoints.SEARCH_PLANETS + name, Planets.class);

        while(planets.getNext() != null){
            results.addAll(planets.getResults());
            String nextPage = planets.getNext().replace("http", "https");
            planets = restTemplate.getForObject(nextPage, Planets.class);
        }
        results.addAll(planets.getResults());

        for (Planet planet : results) {

            List<String> residentsNames = new ArrayList<>();
            for(int i = 0; i < planet.getResidents().size(); i++){
                String resident = planet.getResidents().get(i).replace("http", "https");
                residentsNames.add(restTemplate.getForObject(resident, Person.class).getName());
            }
            planet.setResidents(residentsNames);

            List<String> filmTitles = new ArrayList<>();
            for (int i = 0; i < planet.getFilms().size(); i++) {
                String film = planet.getFilms().get(i).replace("http", "https");
                filmTitles.add(restTemplate.getForObject(film, Film.class).getTitle());
            }
            planet.setFilms(filmTitles);

        }

        modelMap.addAttribute("planet", results.get(0));
        return "/planets/planet";
    }

    @GetMapping("/planets")
    public String searchPlanets(RestTemplate restTemplate, ModelMap modelMap,
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
        return "/planets/planets";
    }
}
