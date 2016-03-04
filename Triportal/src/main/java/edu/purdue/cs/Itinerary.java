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

    public void getDaysInBackground(FindCallback<Day> callback) {
        ParseQuery<Day> query = ParseQuery.getQuery(Day.class);
        query.whereEqualTo("itinerary", this);
        query.orderByAscending("dayIndex");
        query.findInBackground(callback);
    }

    public List<Day> getDays() throws ParseException {
        ParseQuery<Day> query = ParseQuery.getQuery(Day.class);
        query.whereEqualTo("itinerary", this);
        query.orderByAscending("dayIndex");
        return query.find();
    }

    static public void getMyItineraryListInBackground(FindCallback<Itinerary> callback) {
        ParseQuery<Itinerary> query = ParseQuery.getQuery(Itinerary.class);
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.orderByDescending("createdAt");
        query.findInBackground(callback);
    }

    static public List<Itinerary> getMyItineraryList() throws ParseException {
        ParseQuery<Itinerary> query = ParseQuery.getQuery(Itinerary.class);
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.orderByDescending("createdAt");
        return query.find();
    }
}
