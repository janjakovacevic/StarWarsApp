package star.wars.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import star.wars.app.models.films.Film;
import star.wars.app.models.films.Films;
import star.wars.app.models.species.AllSpecies;
import star.wars.app.models.species.Species;
import star.wars.app.utilities.EndPoints;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SpeciesController {

    @GetMapping("/species")
    public String viewAllSpecies(RestTemplate restTemplate, ModelMap modelMap) {

        List<Species> results = new ArrayList<>();
        AllSpecies allSpecies = restTemplate.getForObject(EndPoints.SPECIES, AllSpecies.class);

        while(allSpecies.getNext() != null){
            results.addAll(allSpecies.getResults());
            String nextPage = allSpecies.getNext().replace("http", "https");
            allSpecies = restTemplate.getForObject(nextPage, AllSpecies.class);
        }
        results.addAll(allSpecies.getResults());
        modelMap.addAttribute("count", allSpecies.getCount());
        modelMap.addAttribute("allSpecies", results);
        return "/species";
    }
}
