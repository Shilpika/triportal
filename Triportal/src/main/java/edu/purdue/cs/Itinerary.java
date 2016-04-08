package edu.purdue.cs;

import com.parse.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@ParseClassName("Itinerary")
public class Itinerary extends ParseObject {
    private HashMap<String, Object> _fork() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("id", this.getObjectId());
        return params;
    }

    private ParseQuery<Day> _getDays() {
        ParseQuery<Day> query = ParseQuery.getQuery(Day.class);
        query.whereEqualTo("itinerary", this);
        query.orderByAscending("dayIndex");
        return query;
    }

    static private ParseQuery<Itinerary> _getMyItineraryList() {
        ParseQuery<Itinerary> query = ParseQuery.getQuery(Itinerary.class);
        query.whereEqualTo("owner", ParseUser.getCurrentUser());
        query.orderByDescending("createdAt");
        return query;
    }

    static private ParseQuery<Itinerary> _getSharedItineraryList() {
        ParseQuery<Itinerary> query = ParseQuery.getQuery(Itinerary.class);
        query.orderByDescending("createdAt");
        return query;
    }

    public Itinerary() {
        super();
    }

    public static Itinerary create() {
        Itinerary itinerary = new Itinerary();
        itinerary.setOwner(ParseUser.getCurrentUser());
        return itinerary;
    }

    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    public Date getStartDate() {
        return getDate("startDate");
    }

    public void setStartDate(Date startDate) {
        put("startDate", startDate);
    }

    public ParseUser getOwner() {
        return getParseUser("owner");
    }

    public void setOwner(ParseUser user) {
        put("owner", user);
    }

    public Integer getNumberOfDays() {
        return getInt("numberOfDays");
    }

    public void increaseNumberOfDays(int days) {
        increment("numberOfDays", days);
    }

    public void increaseNumberOfDays() {
        increment("numberOfDays");
    }

    public void getDaysInBackground(FindCallback<Day> callback) {
        ParseQuery<Day> query = _getDays();
        query.findInBackground(callback);
    }

    public void deleteMyItineraryListEventually(DeleteCallback callback) throws ParseException {
        this.unpin();
        this.deleteEventually(callback);
    }

    public List<Day> getDays() throws ParseException {
        ParseQuery<Day> query = _getDays();
        return query.find();
    }

    public void addOneDayInBackground(final GetCallback<Day> callback) {
        final Itinerary thisItinerary = this;
        final Day day = new Day();
        day.add("itinerary", this);
        day.add("dayIndex", getNumberOfDays());
        day.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                    callback.done(null, e);
                else {
                    thisItinerary.increment("numOfDays");
                    thisItinerary.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            callback.done(day, null);
                        }
                    });
                }
            }
        });
    }

    public Day addOneDay() throws ParseException {
        final Day day = new Day();
        day.add("itinerary", this);
        day.add("dayIndex", getNumberOfDays());
        day.save();
        increment("numOfDays");
        save();
        return day;
    }

    public void removeDayAtInBackground(final int index, final SaveCallback callback) {
        int numOfDays = getNumberOfDays();
        if (index >= numOfDays || index < 0) {
            callback.done(new ParseException(ParseException.INCORRECT_TYPE, "Invalid index"));
            return;
        }
        final Itinerary thisItinerary = this;
        ParseQuery<Day> query = ParseQuery.getQuery(Day.class);
        query.whereEqualTo("itinerary", this);
        query.whereGreaterThanOrEqualTo("dayIndex", index);
        query.orderByAscending("dayIndex");
        query.findInBackground(new FindCallback<Day>() {
            @Override
            public void done(List<Day> days, ParseException e) {
                if (e != null) {
                    callback.done(e);
                    return;
                }
                try {
                    for (Day day : days) {
                        if (day.getInt("dayIndex") == index) {
                            day.delete();
                        } else {
                            day.increment("dayIndex", -1);
                            day.save();
                        }
                    }
                } catch(ParseException err) {
                    callback.done(err);
                    return;
                }
                thisItinerary.increment("numOfDays", -1);
                thisItinerary.saveInBackground(callback);
            }
        });
    }

    public void removeDayAt(final int index) throws ParseException {
        int numOfDays = getNumberOfDays();
        if (index >= numOfDays || index < 0) return;
        ParseQuery<Day> query = ParseQuery.getQuery(Day.class);
        query.whereEqualTo("itinerary", this);
        query.whereGreaterThanOrEqualTo("dayIndex", index);
        query.orderByAscending("dayIndex");
        List<Day> days = query.find();
        for (Day day : days) {
            if (day.getInt("dayIndex") == index) {
                day.delete();
            } else {
                day.increment("dayIndex", -1);
                day.save();
            }
        }
        increment("numOfDays", -1);
        save();
    }

    public void forkInBackground(final FunctionCallback<Itinerary> callback) {
        ParseCloud.callFunctionInBackground("fork_itinerary", _fork(), new FunctionCallback<String>() {
            @Override
            public void done(String objectId, ParseException e) {
                if (e != null) {
                    callback.done(null, e);
                    return;
                }
                try {
                    ParseQuery<Itinerary> query = ParseQuery.getQuery(Itinerary.class);
                    Itinerary itinerary = query.get(objectId);
                    callback.done(itinerary, null);
                } catch (ParseException err) {
                    callback.done(null, err);
                }
            }
        });
    }

    public Itinerary fork() throws ParseException {
        String objectId = (String) ParseCloud.callFunction("fork_itinerary", _fork());
        ParseQuery<Itinerary> query = ParseQuery.getQuery(Itinerary.class);
        return query.get(objectId);
    }

    static public Itinerary getById(String id) throws ParseException {
        ParseQuery<Itinerary> query = ParseQuery.getQuery(Itinerary.class);
        query.fromLocalDatastore();
        return query.get(id);
    }

    static public void getByIdInBackground(String id, GetCallback<Itinerary> callback) {
        ParseQuery<Itinerary> query = ParseQuery.getQuery(Itinerary.class);
        query.fromLocalDatastore();
        query.getInBackground(id, callback);
    }

    static public void getMyItineraryListInBackground(FindCallback<Itinerary> callback) {
        ParseQuery<Itinerary> query = _getMyItineraryList();
        query.findInBackground(callback);
    }

    static public List<Itinerary> getMyItineraryList() throws ParseException {
        ParseQuery<Itinerary> query = _getMyItineraryList();
        return query.find();
    }

    static public void getSharedItineraryListInBackground(FindCallback<Itinerary> callback) {
        ParseQuery<Itinerary> query = _getSharedItineraryList();
        query.findInBackground(callback);
    }

    static public List<Itinerary> getSharedItineraryList() throws ParseException {
        ParseQuery<Itinerary> query = _getSharedItineraryList();
        return query.find();
    }
}
