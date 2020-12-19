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
import star.wars.app.models.species.AllSpecies;
import star.wars.app.models.species.Species;
import star.wars.app.utilities.EndPoints;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SpeciesController {

    @GetMapping("/species/details")
    public String viewSpecies(RestTemplate restTemplate, ModelMap modelMap,
                              @RequestParam(value = "name", defaultValue = "") String name) {

        List<Species> results = getAllSpeciesByName(restTemplate, name);

        for(Species species : results) {
            changeURLsToNames(restTemplate, species);
        }


        modelMap.addAttribute("species", results.get(0));
        return "/species/species";
    }

    @GetMapping("/species")
    public String searchSpecies(RestTemplate restTemplate, ModelMap modelMap,
                                @RequestParam(value = "name", defaultValue = "") String name) {

        List<Species> results = getAllSpeciesByName(restTemplate, name);
        modelMap.addAttribute("count", results.size());
        modelMap.addAttribute("allSpecies", results);
        return "/species/all-species";
    }

    private List<Species> getAllSpeciesByName(RestTemplate restTemplate, String name) {
        List<Species> results = new ArrayList<>();
        AllSpecies allSpecies = restTemplate.getForObject(EndPoints.SEARCH_SPECIES + name, AllSpecies.class);

        while (allSpecies.getNext() != null) {
            results.addAll(allSpecies.getResults());
            String nextPage = allSpecies.getNext().replace("http", "https");
            allSpecies = restTemplate.getForObject(nextPage, AllSpecies.class);
        }
        results.addAll(allSpecies.getResults());
        return results;
    }

    private void changeURLsToNames(RestTemplate restTemplate, Species species) {
        setSpeciesHomeworldName(restTemplate, species);
        setSpeciesPeopleNames(restTemplate, species);
        setSpeciesFilmNames(restTemplate, species);
    }

    private void setSpeciesHomeworldName(RestTemplate restTemplate, Species species) {
        if(species.getHomeworld() != null) {
            String homeworld = species.getHomeworld().replace("http", "https");
            species.setHomeworld(restTemplate.getForObject(homeworld, Planet.class).getName());
        }
    }

    private void setSpeciesFilmNames(RestTemplate restTemplate, Species species) {
        List<String> filmTitles = new ArrayList<>();
        for (int i = 0; i < species.getFilms().size(); i++) {
            String film = species.getFilms().get(i).replace("http", "https");
            filmTitles.add(restTemplate.getForObject(film, Film.class).getTitle());
        }
        species.setFilms(filmTitles);
    }

    private void setSpeciesPeopleNames(RestTemplate restTemplate, Species species) {
        List<String> peopleNames = new ArrayList<>();
        for (int i = 0; i < species.getPeople().size(); i++) {
            String person = species.getPeople().get(i).replace("http", "https");
            peopleNames.add(restTemplate.getForObject(person, Person.class).getName());
        }
        species.setPeople(peopleNames);
    }

}
