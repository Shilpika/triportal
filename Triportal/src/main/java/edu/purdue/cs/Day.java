package edu.purdue.cs;

import com.parse.*;

import java.util.List;

@ParseClassName("Day")
public class Day extends ParseObject {
    public Day() {
        super();
    }

    public Integer getDayIndex() {
        return getInt("dayIndex");
    }

    public void getPoiList(FindCallback<Poi> callback) {
        List<Poi> poiList = getList("poiList");
        ParseObject.fetchAllIfNeededInBackground(poiList, callback);
    }

    public List<Poi> getPoiList() throws ParseException {
        List<Poi> poiList = getList("poiList");
        return ParseObject.fetchAll(poiList);
    }
}
