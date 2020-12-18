package star.wars.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import star.wars.app.models.films.Film;
import star.wars.app.models.films.Films;
import star.wars.app.utilities.EndPoints;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FilmsController {

    @GetMapping("/films/details/{title}")
    public String viewFilm(RestTemplate restTemplate, ModelMap modelMap,
                               @PathVariable String title) {

        List<Film> results = new ArrayList<>();
        Films films = restTemplate.getForObject(EndPoints.SEARCH_FILMS + title, Films.class);

        while(films.getNext() != null){
            results.addAll(films.getResults());
            String nextPage = films.getNext().replace("http", "https");
            films = restTemplate.getForObject(nextPage, Films.class);
        }
        results.addAll(films.getResults());
        modelMap.addAttribute("film", results.get(0));
        return "/films/film";
    }

    @GetMapping("/films")
    public String searchFilms(RestTemplate restTemplate, ModelMap modelMap,
                               @RequestParam(value = "title", defaultValue = "") String title) {

        List<Film> results = new ArrayList<>();
        Films films = restTemplate.getForObject(EndPoints.SEARCH_FILMS + title, Films.class);

        while(films.getNext() != null){
            results.addAll(films.getResults());
            String nextPage = films.getNext().replace("http", "https");
            films = restTemplate.getForObject(nextPage, Films.class);
        }
        results.addAll(films.getResults());
        modelMap.addAttribute("count", films.getCount());
        modelMap.addAttribute("films", results);
        return "/films/films";
    }
}
