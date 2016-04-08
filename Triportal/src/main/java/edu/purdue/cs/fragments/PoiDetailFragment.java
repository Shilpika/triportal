package edu.purdue.cs.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;



import edu.purdue.cs.DayListView;
import edu.purdue.cs.Itinerary;
import edu.purdue.cs.*;
import edu.purdue.cs.R;
import edu.purdue.cs.util.ImageDownloadTask;

/**
 * Created by Ge on 16/4/6.
 */
public class PoiDetailFragment extends Fragment {

    ImageView detailimage;
    TextView detailname;
    TextView detaillocation;
    RatingBar detailrating;
    TextView detailDescription;
    TextView detailscore;
    TextView ratingTimes;
    TextView fixedDescirbe;
    Poi poi;

    public static PoiDetailFragment newInstance(){
        return new PoiDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        String poi_id = bundle.getString("poi_id");
        Log.d("PoiDetailFragment","Poi ID: " + poi_id);
        try {
            poi = Poi.getById(poi_id);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        View rootView =  inflater.inflate(R.layout.poi_detail, container, false);
        detailimage = (ImageView) rootView.findViewById(R.id.poi_detail_image);
        detailname = (TextView) rootView.findViewById(R.id.poi_detail_name);
        detaillocation = (TextView)rootView.findViewById(R.id.poi_detail_location);
        detailrating = (RatingBar)rootView.findViewById(R.id.poi_detail_ratingBar);
        detailscore = (TextView)rootView.findViewById(R.id.poi_detail_ratingScore);
        ratingTimes = (TextView)rootView.findViewById(R.id.poi_detail_ratingTimes);
        fixedDescirbe = (TextView)rootView.findViewById(R.id.description);
        detailDescription = (TextView)rootView.findViewById(R.id.poi_detail_description);

        String imageUrl = poi.getImage();
        if (imageUrl == null || imageUrl.isEmpty()) {
            detailimage.setVisibility(View.GONE);
        } else {
            detailimage.setVisibility(View.VISIBLE);
            new ImageDownloadTask(detailimage).execute(poi.getImage());
        }

        detailname.setText(poi.getName());
        detaillocation.setText(poi.getLocationString());

        detailrating.setRating((float)poi.getRating());
        String score = Double.toString(poi.getRating());
        detailscore.setText(score + "/5.0");
        String times = Integer.toString(poi.getNumReviews());
        ratingTimes.setText(times + " Comments");

        if(poi.getDescription().equals("")) {
            fixedDescirbe.setText("");
            detailDescription.setText("");
        } else {
            fixedDescirbe.setText("Description");
            detailDescription.setText(poi.getDescription());
        }

        return rootView;


    }
}
