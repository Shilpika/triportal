package edu.purdue.cs;

import com.parse.*;

import java.util.List;

@ParseClassName("Itinerary")
public class Itinerary extends ParseObject {
    public Itinerary() {
        super();
    }

    public String getTitle() {
        return getString("title");
    }

    public ParseUser getOwner() {
        return getParseUser("owner");
    }

    public Integer getNumberOfDays() {
        return getInt("numberOfDays");
    }

    public List<Day> getDays(){
        return null;
    }
}
