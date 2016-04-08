package edu.purdue.cs;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.util.*;
import android.widget.ArrayAdapter;

import com.parse.ParseAnalytics;
import com.parse.ParseException;

import edu.purdue.cs.Adapter.DayListViewAdapter;
import edu.purdue.cs.fragments.BoardFragment;
import edu.purdue.cs.fragments.PoiDetailFragment;

/**
 * Created by Ge on 16/4/7.
 */
public class PoiDetailView extends AppCompatActivity {
    private Itinerary itinerary;
    private Toolbar toolbar;
    private ListView poilist;
    private String poi_id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.poidetailview);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        //View rootView =  inflater.inflate(R.layout.trip_tab, container, false);
        toolbar = (Toolbar) findViewById(R.id.poi_detail_toolbar);
        setSupportActionBar(toolbar);


        PoiDetailFragment fragment = PoiDetailFragment.newInstance();
        Bundle extras = getIntent().getExtras();
        fragment.setArguments(extras);
        poi_id = extras.getString("poi_id");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.poi_detail_frame, fragment, "fragment").commit();

      /*  Log.d("DayListView It Id", itneraryID);

        try {
            itinerary = Itinerary.getById(itneraryID);
            toolbar.setTitle(itinerary.getTitle());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            List<Day> dayList = itinerary.getDays();
            DayListViewAdapter dayListViewAdapter = new DayListViewAdapter(dayList, getApplicationContext());
            poilist.setAdapter(dayListViewAdapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_poidetail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_action) {
            Intent resultdata = new Intent();
            resultdata.putExtra("poi_id", poi_id);
            setResult(Activity.RESULT_OK, resultdata);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}