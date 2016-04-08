package edu.purdue.cs.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;

import java.util.*;

import edu.purdue.cs.*;

public class PoiListAdapter extends BaseAdapter {
    private final Context context;
    private List<Poi> poiList = new ArrayList<Poi>();

    public PoiListAdapter(Context context) {
        this.context = context;
    }

    public PoiListAdapter(List<Poi> poiList, Context context) {
        this.poiList = poiList;
        this.context = context;
    }

    public void swapList(List<Poi> poiList) {
        this.poiList = poiList;
    }

    @Override
    public int getCount() {
        return poiList.size();
    }

    @Override
    public Object getItem(int position) {
        return poiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.poiview, viewGroup, false);
        }

        TextView poiName = (TextView) convertView.findViewById(R.id.search_poi_name);
        TextView locationString = (TextView) convertView.findViewById(R.id.search_location_string);

        Poi poi = poiList.get(position);

        poiName.setText(poi.getName());
        locationString.setText(poi.getLocationString());

        return convertView;
    }
}
