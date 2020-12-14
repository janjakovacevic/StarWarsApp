package star.wars.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import star.wars.app.models.starships.Starship;
import star.wars.app.models.starships.Starships;
import star.wars.app.utilities.EndPoints;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StarshipsController {

    @GetMapping("/starships")
    public String viewAllStarships(RestTemplate restTemplate, ModelMap modelMap) {

        List<Starship> results = new ArrayList<>();
        Starships starships = restTemplate.getForObject(EndPoints.STARSHIPS, Starships.class);

        while(starships.getNext() != null){
            results.addAll(starships.getResults());
            String nextPage = starships.getNext().replace("http", "https");
            starships = restTemplate.getForObject(nextPage, Starships.class);
        }
        results.addAll(starships.getResults());
        modelMap.addAttribute("count", starships.getCount());
        modelMap.addAttribute("starships", results);
        return "/starships";
    }

}
