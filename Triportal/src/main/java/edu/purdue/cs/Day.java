package edu.purdue.cs;

import com.parse.*;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@ParseClassName("Day")
public class Day extends ParseObject {

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

    public ArrayList<String> getPoiOrder() {
        JSONArray array = getJSONArray("poiOrder");
        ArrayList<String> poiOrder = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                poiOrder.add(array.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return poiOrder;
    }

    public void getPoiListInBackground(final FindCallback<Poi> callback) {
        ParseQuery<Poi> query = _getPoiList();
        query.findInBackground(new FindCallback<Poi>() {
            @Override
            public void done(List<Poi> poiList, ParseException e) {
                if (e != null) {
                    callback.done(null, e);
                    return;
                }
                final ArrayList<String> poiOrder = getPoiOrder();
                Collections.sort(poiList, new Comparator<Poi>() {
                    @Override
                    public int compare(Poi lhs, Poi rhs) {
                        int lindex = poiOrder.indexOf(lhs.getObjectId());
                        int rindex = poiOrder.indexOf(rhs.getObjectId());
                        if ( lindex - rindex > 0 ) return 1;
                        else if (lindex - rindex < 0) return -1;
                        else return 0;
                    }
                });
                callback.done(poiList, null);
            }
        });
    }

    public List<Poi> getPoiList() throws ParseException {
        ParseQuery<Poi> query = _getPoiList();
        List<Poi> poiList = query.find();
        final ArrayList<String> poiOrder = getPoiOrder();
        Collections.sort(poiList, new Comparator<Poi>() {
            @Override
            public int compare(Poi lhs, Poi rhs) {
                int lindex = poiOrder.indexOf(lhs.getObjectId());
                int rindex = poiOrder.indexOf(rhs.getObjectId());
                if ( lindex - rindex > 0 ) return 1;
                else if (lindex - rindex < 0) return -1;
                else return 0;
            }
        });
        return poiList;
    }

    public void setPoiList(final List<Poi> poiList) throws ParseException {
        ParseRelation<Poi> relation = this.getRelation("poiList");
        ParseQuery<Poi> query = relation.getQuery();
        List<Poi> originalPoiList = query.find();
        ArrayList<String> poiOrder = new ArrayList<>();
        for (int i = 0; i < poiList.size(); i++) {
            Poi poi = poiList.get(i);
            if (originalPoiList.indexOf(poi) < 0) {
                relation.add(poi);
            }
            poiOrder.add(poi.getObjectId());
        }
        for (Poi poi : originalPoiList) {
            if (poiOrder.indexOf(poi.getObjectId()) < 0) {
                relation.remove(poi);
            }
        }
        remove("poiList");
        add("poiList", relation);
        remove("poiOrder");
        add("poiOrder", poiOrder);
    }

    public void setItinerary(Itinerary itinerary) {
        put("itinerary", itinerary);
    }
}
