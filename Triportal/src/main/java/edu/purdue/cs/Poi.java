package edu.purdue.cs;

import com.parse.*;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Poi")
public class Poi extends ParseObject {

    public Poi() {
        super();
    }

    public String getName() {
        return getString("name");
    }

    public String getDescription() {
        return getString("description");
    }

    public double getRating() {
        return getDouble("rating");
    }

    public int getNumReviews() {
        return getInt("num_reviews");
    }

    public String getLocationString () {
        return getString("location_string");
    }

    public int getLocationId () {
        return getInt("location_id");
    }

    static public Poi getById(String id) throws ParseException {
        ParseQuery<Poi> query = ParseQuery.getQuery(Poi.class);
        query.fromLocalDatastore();
        return query.get(id);
    }

    public String getImage() {
        return getString("image");
    }

    private ParseQuery<Poi> _search(String input) {
        ParseQuery<Poi> locationStringQuery = ParseQuery.getQuery(Poi.class);
        ParseQuery<Poi> nameQuery = ParseQuery.getQuery(Poi.class);
        locationStringQuery.whereMatches("location_string", input, "i");
        nameQuery.whereMatches("name", input, "i");
        List<ParseQuery<Poi>> queries = new ArrayList<>();
        queries.add(locationStringQuery);
        queries.add(nameQuery);
        ParseQuery<Poi> mainQuery = ParseQuery.or(queries);
        mainQuery.orderByDescending("num_reviews,rating");
        mainQuery.addAscendingOrder("name");
        mainQuery.setLimit(50);
        return mainQuery;
    }

    public void searchInBackground(String input, FindCallback<Poi> callback) {
        ParseQuery<Poi> query = _search(input);
        query.findInBackground(callback);
    }

    public List<Poi> search(String input) throws ParseException {
        ParseQuery<Poi> query = _search(input);
        return query.find();
    }

    public ParseGeoPoint getGeoPoint() {
        return getParseGeoPoint("geo_point");
    }
}
