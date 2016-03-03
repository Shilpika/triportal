package edu.purdue.cs;

import com.parse.*;

@ParseClassName("Instrument")
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

    public Integer getRating() {
        return getInt("rating");
    }

    public String getImageUrl() {
        return getString("image");
    }

    public Double getLatitude() {
        return getDouble("latitude");
    }

    public Double getLongitude() {
        return getDouble("longitude");
    }
}
