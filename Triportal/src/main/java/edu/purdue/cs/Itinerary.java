package edu.purdue.cs;

import com.parse.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@ParseClassName("Itinerary")
public class Itinerary extends ParseObject {
    private Itinerary _fork() {
        Itinerary itinerary = new Itinerary();
        Set<String> keys = this.keySet();
        for (String key : keys) {
            itinerary.put(key, this.get(key));
        }
        itinerary.setOwner(ParseUser.getCurrentUser());
        return itinerary;
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

    public Itinerary forkInBackground(final SaveCallback callback) {
        final Itinerary itinerary = _fork();
        itinerary.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                itinerary.getDaysInBackground(new FindCallback<Day>() {
                    @Override
                    public void done(List<Day> days, ParseException e) {
                        final Integer[] daysFinished = {0};
                        final Integer numberOfDays = itinerary.getNumberOfDays();
                        for (Day day: days) {
                            day.forkInBackground(itinerary, new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    daysFinished[0]++;
                                    if ( daysFinished[0] == numberOfDays ) {
                                        callback.done(null);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        return itinerary;
    }

    public Itinerary fork() throws ParseException {
        Itinerary itinerary = _fork();
        itinerary.save();
        List<Day> days = itinerary.getDays();
        for (Day day: days) {
            day.fork(itinerary);
        }
        return itinerary;
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
}
