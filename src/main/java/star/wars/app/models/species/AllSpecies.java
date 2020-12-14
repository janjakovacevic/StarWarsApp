package star.wars.app.models.species;

import star.wars.app.models.planets.Planet;

import java.util.List;

public class AllSpecies {

    String count;
    String next;
    String previous;
    List<Species> results;

    public AllSpecies() {
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Species> getResults() {
        return results;
    }

    public void setResults(List<Species> results) {
        this.results = results;
    }
}
