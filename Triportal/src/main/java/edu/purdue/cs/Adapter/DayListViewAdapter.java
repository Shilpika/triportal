package edu.purdue.cs.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
public class DayListViewAdapter extends BaseAdapter{
    private Context context;
    private List<Day> dayList;
    private ListView poilistview;

    public DayListViewAdapter(List<Day> dayList, Context context) {
        Log.d("tag", "Daylistcount"+dayList.size());
        this.dayList = dayList;
        this.context = context;
    }

    @Override
    public int getCount() {  return dayList.size(); }
    @Override
    public Object getItem(int i) { return dayList.get(i);}
    @Override
    public long getItemId(int i) { return i;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("tag", ""+position);
        ViewHolder viewHolder;
        View view = convertView;
        ViewGroup viewGroup = parent;
        if (view != null){
            viewHolder = (ViewHolder)view.getTag();
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.poilistview, null);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
        }
        Log.d("viewtag", "view tag set");
        //TextView temp;
        //temp = (TextView) view.findViewById(R.id.date);
        viewHolder.textdate = (TextView) view.findViewById(R.id.date);
        viewHolder.poilist = (ListView) view.findViewById(R.id.poi_list);

        String str = "Day"+ position; //Day + number
        ((TextView) view.findViewById(R.id.date)).setText(str);
        //viewHolder.textdate.setText(str);
        //poilistview = (ListView) ((DayListView)context).findViewById(R.id.poi_list);
        //PoiListAdapter
        PoiListAdapter adapter = null;
        try {
            adapter = new PoiListAdapter(dayList.get(position).getPoiList(), context);
            viewHolder.poilist.setAdapter(adapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.poilist.setAdapter(adapter);
        //根据PoilistView的高度计算Daylist高度
        setListViewHeightBasedOnChildren(viewHolder.poilist);
        return view;
    }

    static class ViewHolder {
        TextView textdate;
        ListView poilist;
    }

    /**
     * @param->listview
     * 此方法是本次listview嵌套listview的核心方法：计算parentlistview item的高度。
     * 如果不使用此方法，无论innerlistview有多少个item，则只会显示一个item。
     **/
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {        return;    }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
