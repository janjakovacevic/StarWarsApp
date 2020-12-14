package star.wars.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import star.wars.app.models.starships.Starship;
import star.wars.app.models.starships.Starships;
import star.wars.app.models.vehicles.Vehicle;
import star.wars.app.models.vehicles.Vehicles;
import star.wars.app.utilities.EndPoints;

import java.util.ArrayList;
import java.util.List;

@Controller
public class VehiclesController {

    @GetMapping("/vehicles")
    public String viewAllVehicles(RestTemplate restTemplate, ModelMap modelMap) {

        List<Vehicle> results = new ArrayList<>();
        Vehicles vehicles = restTemplate.getForObject(EndPoints.VEHICLES, Vehicles.class);

        while(vehicles.getNext() != null){
            results.addAll(vehicles.getResults());
            String nextPage = vehicles.getNext().replace("http", "https");
            vehicles = restTemplate.getForObject(nextPage, Vehicles.class);
        }
        results.addAll(vehicles.getResults());
        modelMap.addAttribute("count", vehicles.getCount());
        modelMap.addAttribute("vehicles", results);
        return "/vehicles";
    }

}
