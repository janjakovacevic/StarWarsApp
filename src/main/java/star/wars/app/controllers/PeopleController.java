package star.wars.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import star.wars.app.models.films.Film;
import star.wars.app.models.people.People;
import star.wars.app.models.people.Person;
import star.wars.app.models.planets.Planet;
import star.wars.app.models.species.Species;
import star.wars.app.models.starships.Starship;
import star.wars.app.models.vehicles.Vehicle;
import star.wars.app.utilities.EndPoints;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PeopleController {

    @GetMapping("/people/details")
    public String viewPerson(RestTemplate restTemplate, ModelMap modelMap,
                             @RequestParam(value = "name", defaultValue = "") String name) {

        List<Person> results = getAllPeopleByName(restTemplate, name);
        for(Person person : results){
            changeURLsToNames(restTemplate, person);
        }

        modelMap.addAttribute("person", results.get(0));
        return "/people/person";
    }

    @GetMapping("/people")
    public String searchPeople(RestTemplate restTemplate, ModelMap modelMap,
                               @RequestParam(value = "name", defaultValue = "") String name) {

        List<Person> results = getAllPeopleByName(restTemplate, name);
        modelMap.addAttribute("count", results.size());
        modelMap.addAttribute("people", results);
        return "/people/people";
    }

    private void changeURLsToNames(RestTemplate restTemplate, Person person) {
        setPeopleHomeworldNames(restTemplate, person);
        setPeopleFilmNames(restTemplate, person);
        setPeopleSpeciesNames(restTemplate, person);
        setPeopleVehicleNames(restTemplate, person);
        setPeopleStarshipNames(restTemplate, person);
    }

    private void setPeopleStarshipNames(RestTemplate restTemplate, Person person) {
        List<String> starshipNames = new ArrayList<>();
        for(int i = 0; i < person.getStarships().size(); i++){
            String starship = person.getStarships().get(i).replace("http", "https");
            starshipNames.add(restTemplate.getForObject(starship, Starship.class).getName());
        }
        person.setStarships(starshipNames);
    }

    private void setPeopleVehicleNames(RestTemplate restTemplate, Person person) {
        List<String> vehicleNames = new ArrayList<>();
        for(int i = 0; i < person.getVehicles().size(); i++){
            String vehicle = person.getVehicles().get(i).replace("http", "https");
            vehicleNames.add( restTemplate.getForObject(vehicle, Vehicle.class).getName());
        }
        person.setVehicles(vehicleNames);
    }

    private void setPeopleSpeciesNames(RestTemplate restTemplate, Person person) {
        List<String> speciesNames = new ArrayList<>();
        for(int i = 0; i < person.getSpecies().size(); i++){
            String species = person.getSpecies().get(i).replace("http", "https");
            speciesNames.add(restTemplate.getForObject(species, Species.class).getName());
        }
        person.setSpecies(speciesNames);
    }

    private void setPeopleFilmNames(RestTemplate restTemplate, Person person) {
        List<String> filmTitles = new ArrayList<>();
        for(int i = 0; i < person.getFilms().size(); i++){
            String film = person.getFilms().get(i).replace("http", "https");
            filmTitles.add(restTemplate.getForObject(film, Film.class).getTitle());
        }
        person.setFilms(filmTitles);
    }

    private void setPeopleHomeworldNames(RestTemplate restTemplate, Person person) {
        if(person.getHomeworld() != null) {
            String homeworld = person.getHomeworld().replace("http", "https");
            person.setHomeworld(restTemplate.getForObject(homeworld, Planet.class).getName());
        }
    }

    private List<Person> getAllPeopleByName(RestTemplate restTemplate, String name) {
        People people = restTemplate.getForObject(EndPoints.SEARCH_PEOPLE + name, People.class);

        List<Person> results = new ArrayList<>();
        while (people.getNext() != null) {
            results.addAll(people.getResults());
            String nextPage = people.getNext().replace("http", "https");
            people = restTemplate.getForObject(nextPage, People.class);
        }
        results.addAll(people.getResults());

        return results;
    }

}
