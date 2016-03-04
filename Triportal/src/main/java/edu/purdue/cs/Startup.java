package edu.purdue.cs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;

import java.util.List;

import edu.purdue.cs.fragments.SlidingTabsFragment;
import edu.purdue.cs.fragments.TripTabFragment;
import edu.purdue.cs.util.view.SlidingTabLayout;


public class Startup extends AppCompatActivity {

    private SlidingTabsFragment StartupTabFragmentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        this.StartupTabFragmentView = (SlidingTabsFragment) getSupportFragmentManager().findFragmentById(R.id.activity_startup_fragment);
        /**
         * set up toolbar
         */
        Toolbar actionbar = (Toolbar) findViewById(R.id.startupActionBar);
        setSupportActionBar(actionbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        assert (StartupTabFragmentView != null);

        Log.d("Itinerary", "\nSynchronous Call...\n");

        try {
            List<Itinerary> itineraryList = Itinerary.getMyItineraryList();
            for (Itinerary itinerary : itineraryList) {
                Log.d("Itinerary", "Title: " + itinerary.getTitle());
                List<Day> dayList = itinerary.getDays();
                for (Day day: dayList) {
                    Log.d("Itinerary", "Day: " + day.getDayIndex());
                    List<Poi> poiList = day.getPoiList();
                    for (Poi poi: poiList) {
                        Log.d("Itinerary", "Poi: " + poi.getName());
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.d("Itinerary", "\nAsynchronous Call...\n");

        Itinerary.getMyItineraryListInBackground(new FindCallback<Itinerary>() {
            @Override
            public void done(List<Itinerary> itineraryList, ParseException e) {
                for (Itinerary itinerary : itineraryList) {
                    Log.d("Itinerary", "Title: " + itinerary.getTitle());
                    itinerary.getDaysInBackground(new FindCallback<Day>() {
                        @Override
                        public void done(List<Day> dayList, ParseException e) {
                            for (Day day: dayList) {
                                Log.d("Itinerary", "Day: " + day.getDayIndex());
                                day.getPoiListInBackground(new FindCallback<Poi>() {
                                    @Override
                                    public void done(List<Poi> poiList, ParseException e) {
                                        for (Poi poi: poiList) {
                                            Log.d("Itinerary", "Poi: " + poi.getName());
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_starup, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      // Handle action bar item clicks here. The action bar will
      // automatically handle clicks on the Home/Up button, so long
      // as you specify a parent activity in AndroidManifest.xml.
      int id = item.getItemId();

      //noinspection SimplifiableIfStatement
      if (id == R.id.action_user_settings) {
          Intent user = new Intent(Startup.this, UserProfile.class);
          startActivity(user);
          //finish();
          return true;
      }
      return false;
  }

}

