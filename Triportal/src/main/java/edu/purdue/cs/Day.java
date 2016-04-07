package edu.purdue.cs;

import com.parse.*;

import java.util.List;
import java.util.Set;

@ParseClassName("Day")
public class Day extends ParseObject {
    private Day _fork(Itinerary itinerary) {
        Day day = new Day();
        Set<String> keys = this.keySet();
        for (String key : keys) {
            day.put(key, this.get(key));
        }
        day.setItinerary(itinerary);
        return day;
    }

    private ParseQuery<Poi> _getPoiList() {
        ParseRelation<Poi> relation = this.getRelation("poiList");
        return relation.getQuery();
    }

    public Day() {
        super();
    }

    public Integer getDayIndex() {
        return getInt("dayIndex");
    }

    public void getPoiListInBackground(FindCallback<Poi> callback) {
        ParseQuery<Poi> query = _getPoiList();
        query.findInBackground(callback);
    }

    public List<Poi> getPoiList() throws ParseException {
        ParseQuery<Poi> query = _getPoiList();
        return query.find();
    }

    public Day fork(Itinerary itinerary) throws ParseException {
        Day day = _fork(itinerary);
        day.save();
        return day;
    }

    public void forkInBackground(Itinerary itinerary, SaveCallback callback) {
        Day day = _fork(itinerary);
        day.saveInBackground(callback);
    }

    public void setItinerary(Itinerary itinerary) {
        put("itinerary", itinerary);
    }
}
