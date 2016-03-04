package edu.purdue.cs.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;

import java.util.*;
import edu.purdue.cs.*;

/**
 * Created by Ge on 16/3/3.
 */
public class PoiListAdapter extends BaseAdapter {
    private Context context;
    private List<Poi> poiList = new ArrayList<Poi>();

    public PoiListAdapter(List<Poi> poiList, Context context) {
        this.poiList= poiList;
        this.context = context;
    }

    @Override
    public int getCount() {  return poiList.size(); }
    @Override
    public Object getItem(int i) { return poiList.get(i);}
    @Override
    public long getItemId(int i) { return i;}

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view != null){
            viewHolder = (ViewHolder)view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.poiview, null);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
        }

        viewHolder.poi_detail = (TextView)view.findViewById(R.id.poi);
        Poi temp_poi = poiList.get(i);
        String str = i + "   "+ temp_poi.getName();
        ((TextView)view.findViewById(R.id.poi)).setText(str);

        return view;
    }

    static class ViewHolder {
        private TextView poi_detail;
    }
}
