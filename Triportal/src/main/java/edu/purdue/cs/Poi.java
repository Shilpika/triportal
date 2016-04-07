package edu.purdue.cs;

import com.parse.*;

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

    public ParseGeoPoint getGeoPoint() {
        return getParseGeoPoint("geo_point");
    }
}
