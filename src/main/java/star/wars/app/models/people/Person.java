package star.wars.app.models.people;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import star.wars.app.models.films.Film;
import star.wars.app.models.species.Species;
import star.wars.app.models.starships.Starship;
import star.wars.app.models.vehicles.Vehicle;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    String name;
    String height;
    String mass;
    String hair_color;
    String skin_color;
    String eye_color;
    String birth_year;
    String gender;
//    String homeworld;
//    List<Film> films;
//    List<Species> species;
//    List<Vehicle> vehicles;
//    List<Starship> starships;



//    "homeworld": "http://swapi.dev/api/planets/1/",
//            "films": [
//            "http://swapi.dev/api/films/1/",
//            "http://swapi.dev/api/films/2/",
//            "http://swapi.dev/api/films/3/",
//            "http://swapi.dev/api/films/4/",
//            "http://swapi.dev/api/films/5/",
//            "http://swapi.dev/api/films/6/"
//            ],
//            "species": [
//            "http://swapi.dev/api/species/2/"
//            ],
//            "vehicles": [],
//            "starships": [],
//            "created": "2014-12-10T15:10:51.357000Z",
//            "edited": "2014-12-20T21:17:50.309000Z",
//            "url": "http://swapi.dev/api/people/2/"



    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getMass() {
        return mass;
    }

    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getHair_color() {
        return hair_color;
    }

    public void setHair_color(String hair_color) {
        this.hair_color = hair_color;
    }

    public String getSkin_color() {
        return skin_color;
    }

    public void setSkin_color(String skin_color) {
        this.skin_color = skin_color;
    }

    public String getEye_color() {
        return eye_color;
    }

    public void setEye_color(String eye_color) {
        this.eye_color = eye_color;
    }

    public String getBirth_year() {
        return birth_year;
    }

    public void setBirth_year(String birth_year) {
        this.birth_year = birth_year;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
