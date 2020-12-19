package star.wars.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import star.wars.app.models.films.Film;
import star.wars.app.models.people.Person;
import star.wars.app.models.starships.Starship;
import star.wars.app.models.starships.Starships;
import star.wars.app.utilities.EndPoints;

import java.util.ArrayList;
import java.util.List;

@Controller
public class StarshipsController {

    @GetMapping("/starships/details")
    public String viewVehicle(RestTemplate restTemplate, ModelMap modelMap,
                              @RequestParam(value = "name", defaultValue = "") String name) {

        List<Starship> results = new ArrayList<>();
        Starships starships = restTemplate.getForObject(EndPoints.SEARCH_STARSHIPS + name, Starships.class);

        while(starships.getNext() != null){
            results.addAll(starships.getResults());
            String nextPage = starships.getNext().replace("http", "https");
            starships = restTemplate.getForObject(nextPage, Starships.class);
        }
        results.addAll(starships.getResults());

        for (Starship starship : results) {

            List<String> pilotsNames = new ArrayList<>();
            for(int i = 0; i < starship.getPilots().size(); i++){
                String pilot = starship.getPilots().get(i).replace("http", "https");
                pilotsNames.add(restTemplate.getForObject(pilot, Person.class).getName());
            }
            starship.setPilots(pilotsNames);

            List<String> filmTitles = new ArrayList<>();
            for (int i = 0; i < starship.getFilms().size(); i++) {
                String film = starship.getFilms().get(i).replace("http", "https");
                filmTitles.add(restTemplate.getForObject(film, Film.class).getTitle());
            }
            starship.setFilms(filmTitles);

        }

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
        modelMap.addAttribute("count", results.size());
        modelMap.addAttribute("starships", results);
        return "/starships/starships";
    }

}
