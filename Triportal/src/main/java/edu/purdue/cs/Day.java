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

    public List<Poi> getPoiList() {
        return getList("poiList");
    }
}
