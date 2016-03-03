package edu.purdue.cs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.*;
import android.widget.ArrayAdapter;

import com.parse.ParseAnalytics;

import edu.purdue.cs.Adapter.DayListViewAdapter;

/**
 * Created by Ge on 16/3/3.
 */
public class DayListView extends Activity{
    private Itinerary itinerary;
    private Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.daylistview);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        toolbar = (Toolbar) findViewById(R.id.startupActionBar);
        getData(itinerary);
    }


    private void getData(Itinerary data){
        itinerary = data;
        toolbar.setTitle(itinerary.getTitle());
        for(int i = 0; i < itinerary.getNumberOfDays(); i++){
            DayListViewAdapter dayListViewAdapter = new DayListViewAdapter(itinerary.getDays(), this);
            dayListViewAdapter.getView(i)
        }
    }
}
