package star.wars.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import star.wars.app.models.starships.Starship;
import star.wars.app.models.starships.Starships;
import star.wars.app.utilities.EndPoints;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StarshipsController {

    @GetMapping("/starships/details/{name}")
    public String viewVehicle(RestTemplate restTemplate, ModelMap modelMap,
                              @PathVariable String name) {

        List<Starship> results = new ArrayList<>();
        Starships starships = restTemplate.getForObject(EndPoints.SEARCH_STARSHIPS + name, Starships.class);

        while(starships.getNext() != null){
            results.addAll(starships.getResults());
            String nextPage = starships.getNext().replace("http", "https");
            starships = restTemplate.getForObject(nextPage, Starships.class);
        }
        results.addAll(starships.getResults());
        modelMap.addAttribute("starship", results.get(0));
        return "/starships/starship";
    }

    @GetMapping("/starships")
    public String searchStarships(RestTemplate restTemplate, ModelMap modelMap,
                                   @RequestParam(value = "query", defaultValue = "") String query) {

        List<Starship> results = new ArrayList<>();
        Starships starships = restTemplate.getForObject(EndPoints.SEARCH_STARSHIPS + query, Starships.class);

        while(starships.getNext() != null){
            results.addAll(starships.getResults());
            String nextPage = starships.getNext().replace("http", "https");
            starships = restTemplate.getForObject(nextPage, Starships.class);
        }
        results.addAll(starships.getResults());
        modelMap.addAttribute("count", starships.getCount());
        modelMap.addAttribute("starships", results);
        return "/starships/starships";
    }

}
