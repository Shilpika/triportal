package edu.purdue.cs;

import com.parse.*;

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
}
