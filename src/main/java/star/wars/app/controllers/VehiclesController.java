package star.wars.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import star.wars.app.models.films.Film;
import star.wars.app.models.people.Person;
import star.wars.app.models.vehicles.Vehicle;
import star.wars.app.models.vehicles.Vehicles;
import star.wars.app.utilities.EndPoints;

import java.util.ArrayList;
import java.util.List;

@Controller
public class VehiclesController {

    @GetMapping("/vehicles/details")
    public String viewVehicle(RestTemplate restTemplate, ModelMap modelMap,
                              @RequestParam(value = "name", defaultValue = "") String name) {

        List<Vehicle> results = new ArrayList<>();
        Vehicles vehicles = restTemplate.getForObject(EndPoints.SEARCH_VEHICLES + name, Vehicles.class);

        while (vehicles.getNext() != null) {
            results.addAll(vehicles.getResults());
            String nextPage = vehicles.getNext().replace("http", "https");
            vehicles = restTemplate.getForObject(nextPage, Vehicles.class);
        }
        results.addAll(vehicles.getResults());

        for (Vehicle vehicle : results) {

            List<String> pilotsNames = new ArrayList<>();
            for(int i = 0; i < vehicle.getPilots().size(); i++){
                String pilot = vehicle.getPilots().get(i).replace("http", "https");
                pilotsNames.add(restTemplate.getForObject(pilot, Person.class).getName());
            }
            vehicle.setPilots(pilotsNames);

            List<String> filmTitles = new ArrayList<>();
            for (int i = 0; i < vehicle.getFilms().size(); i++) {
                String film = vehicle.getFilms().get(i).replace("http", "https");
                filmTitles.add(restTemplate.getForObject(film, Film.class).getTitle());
            }
            vehicle.setFilms(filmTitles);

        }

        modelMap.addAttribute("vehicle", results.get(0));
        return "/vehicles/vehicle";
    }

    @GetMapping("/vehicles")
    public String searchVehicles(RestTemplate restTemplate, ModelMap modelMap,
                                  @RequestParam(value = "query", defaultValue = "") String query) {

        List<Vehicle> results = new ArrayList<>();
        Vehicles vehicles = restTemplate.getForObject(EndPoints.SEARCH_VEHICLES + query, Vehicles.class);

        while(vehicles.getNext() != null){
            results.addAll(vehicles.getResults());
            String nextPage = vehicles.getNext().replace("http", "https");
            vehicles = restTemplate.getForObject(nextPage, Vehicles.class);
        }
        results.addAll(vehicles.getResults());
        modelMap.addAttribute("count", results.size());
        modelMap.addAttribute("vehicles", results);
        return "/vehicles/vehicles";
    }

}
