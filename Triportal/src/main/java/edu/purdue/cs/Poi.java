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

    public Double getRating() {
        return getDouble("rating");
    }

    public ParseFile getImage() {
        return getParseFile("image");
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }
}
