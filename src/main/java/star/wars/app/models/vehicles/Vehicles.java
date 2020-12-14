package star.wars.app.models.vehicles;


import java.util.List;

public class Vehicles {

    String count;
    String next;
    String previous;
    List<Vehicle> results;

    public Vehicles() {
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

    public List<Vehicle> getResults() {
        return results;
    }

    public void setResults(List<Vehicle> results) {
        this.results = results;
    }
}
