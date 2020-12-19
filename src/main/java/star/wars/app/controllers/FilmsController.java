package star.wars.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import star.wars.app.models.films.Film;
import star.wars.app.models.films.Films;
import star.wars.app.models.people.Person;
import star.wars.app.models.planets.Planet;
import star.wars.app.models.species.Species;
import star.wars.app.models.starships.Starship;
import star.wars.app.models.vehicles.Vehicle;
import star.wars.app.utilities.EndPoints;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FilmsController {

    @GetMapping("/films/details")
    public String viewFilm(RestTemplate restTemplate, ModelMap modelMap,
                           @RequestParam(value = "title", defaultValue = "") String title) {

        List<Film> results = getAllFilmsByTitle(restTemplate, title);
        for(Film film : results){
            changeURLsToNames(restTemplate, film);
        }

        modelMap.addAttribute("film", results.get(0));
        return "/films/film";
    }


    @GetMapping("/films")
    public String searchFilms(RestTemplate restTemplate, ModelMap modelMap,
                               @RequestParam(value = "title", defaultValue = "") String title) {

        List<Film> results = getAllFilmsByTitle(restTemplate, title);

        modelMap.addAttribute("count", results.size());
        modelMap.addAttribute("films", results);
        return "/films/films";
    }

    private void setFilmSpeciesNames(RestTemplate restTemplate, Film film) {
        List<String> speciesNames = new ArrayList<>();
        for(int i = 0; i < film.getSpecies().size(); i++){
            String species = film.getSpecies().get(i).replace("http", "https");
            speciesNames.add(restTemplate.getForObject(species, Species.class).getName());
        }
        film.setSpecies(speciesNames);
    }

    private void setFilmVehicleNames(RestTemplate restTemplate, Film film) {
        List<String> vehicleNames = new ArrayList<>();
        for(int i = 0; i < film.getVehicles().size(); i++){
            String vehicle = film.getVehicles().get(i).replace("http", "https");
            vehicleNames.add(restTemplate.getForObject(vehicle, Vehicle.class).getName());
        }
        film.setVehicles(vehicleNames);
    }

    private void setFilmStarshipNames(RestTemplate restTemplate, Film film) {
        List<String> starshipNames = new ArrayList<>();
        for(int i = 0; i < film.getStarships().size(); i++){
            String starship = film.getStarships().get(i).replace("http", "https");
            starshipNames.add(restTemplate.getForObject(starship, Starship.class).getName());
        }
        film.setStarships(starshipNames);
    }

    private void setFilmPlanetNames(RestTemplate restTemplate, Film film) {
        List<String> planetNames = new ArrayList<>();
        for(int i = 0; i < film.getPlanets().size(); i++) {
            String planet = film.getPlanets().get(i).replace("http", "https");
            planetNames.add(restTemplate.getForObject(planet, Planet.class).getName());
        }
        film.setPlanets(planetNames);
    }

    private void setFilmCharacterNames(RestTemplate restTemplate, Film film) {
        List<String> characterNames = new ArrayList<>();
        for(int i = 0; i < film.getCharacters().size(); i++){
            String character = film.getCharacters().get(i).replace("http", "https");
            characterNames.add(restTemplate.getForObject(character, Person.class).getName());
        }
        film.setCharacters(characterNames);
    }

    private void changeURLsToNames(RestTemplate restTemplate, Film film) {
        setFilmCharacterNames(restTemplate, film);
        setFilmPlanetNames(restTemplate, film);
        setFilmStarshipNames(restTemplate, film);
        setFilmVehicleNames(restTemplate, film);
        setFilmSpeciesNames(restTemplate, film);
    }

    private List<Film> getAllFilmsByTitle(RestTemplate restTemplate, String title) {
        Films films = restTemplate.getForObject(EndPoints.SEARCH_FILMS + title, Films.class);

        List<Film> results = new ArrayList<>();
        while(films.getNext() != null){
            results.addAll(films.getResults());
            String nextPage = films.getNext().replace("http", "https");
            films = restTemplate.getForObject(nextPage, Films.class);
        }
        results.addAll(films.getResults());
        return results;
    }

}
