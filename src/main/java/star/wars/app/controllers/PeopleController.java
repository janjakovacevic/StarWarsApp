package star.wars.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import star.wars.app.models.people.People;
import star.wars.app.models.people.Person;
import star.wars.app.utilities.EndPoints;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PeopleController {

    @GetMapping("/people/details/{name}")
    public String viewPerson(RestTemplate restTemplate, ModelMap modelMap,
                             @PathVariable String name) {
        List<Person> results = new ArrayList<>();
        People people = restTemplate.getForObject(EndPoints.SEARCH_PEOPLE + name, People.class);

        while(people.getNext() != null){
            results.addAll(people.getResults());
            String nextPage = people.getNext().replace("http", "https");
            people = restTemplate.getForObject(nextPage, People.class);
        }
        results.addAll(people.getResults());
        modelMap.addAttribute("person", results.get(0));
        return "/people/person";
    }

    @GetMapping("/people")
    public String searchPeople(RestTemplate restTemplate, ModelMap modelMap,
                               @RequestParam(value = "name", defaultValue = "") String name) {

        List<Person> results = new ArrayList<>();
        People people = restTemplate.getForObject(EndPoints.SEARCH_PEOPLE + name, People.class);

        while(people.getNext() != null){
            results.addAll(people.getResults());
            String nextPage = people.getNext().replace("http", "https");
            people = restTemplate.getForObject(nextPage, People.class);
//            people = restTemplate.getForObject(people.getNext(), People.class);

        }
        results.addAll(people.getResults());
        modelMap.addAttribute("count", people.getCount());
        modelMap.addAttribute("people", results);
        return "/people/people";
    }



}
