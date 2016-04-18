/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.purdue.cs.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import edu.purdue.cs.*;
import edu.purdue.cs.util.template.TabFragment;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;


public class SharedItineraryListFragment extends TabFragment {

    /**
     * inflating view of tab contents
     */

    private ListView tripList;
    private ImageButton addBtn;
    private List<Itinerary> itineraryList;
    private Context pContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.trip_tab, container, false);
        //inflating the card-style list
        tripList = (ListView) rootView.findViewById(R.id.trip_list);
        addBtn = (ImageButton) rootView.findViewById(R.id.trip_tab_create_i_btn);
        addBtn.setVisibility(View.GONE);
        setupList();

        return rootView;

    }

    /*public void setupButton() {



        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Link crate button to the CreateItineraryView
                Intent i = new Intent(getActivity(),CreateItineraryView.class);
                getActivity().startActivity(i);
                // getActivity().finish();
            }
        });
    }*/




    @Override
    public void onResume() {
        super.onResume();

        refreshList();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        if (args != null) {
            return;
            //TODO: arguments to be implement
        }
    }

    private void setupList() {
        Itinerary.getSharedItineraryListInBackground(new FindCallback<Itinerary>() {
            @Override
            public void done(List<Itinerary> objects, ParseException e) {
                itineraryList = objects;
                // assert(itineraryList.size() != 0);
                tripList.setAdapter(createAdapter());
                tripList.setOnItemClickListener(new ListItemClickListener());
            }
        });
    }

    private void refreshList() {
        Itinerary.getSharedItineraryListInBackground(new FindCallback<Itinerary>() {
            @Override
            public void done(List<Itinerary> objects, ParseException e) {
                if(objects == null) return;
                itineraryList = objects;
                SharedItineraryListAdapter adapter = (SharedItineraryListAdapter) tripList.getAdapter();
                if(adapter == null) return;
                adapter.updateList(itineraryList);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void viewListItem(int index) {
        // this should be working since the order of the List View is base on the ItneraryList we fetch from server
        Itinerary itinerary = itineraryList.get(index);
        // make sure this itinerary is pin in local storage
        Log.d("Display Itinerary", itinerary.getTitle());
        Toast.makeText(getActivity(), "Fetching Data.." , Toast.LENGTH_SHORT).show();
        itinerary.pinInBackground();
        Intent intent = new Intent(getActivity(), DayListView.class);
        intent.putExtra("it_ID",itinerary.getObjectId());
        getActivity().startActivity(intent);

    }


    private SharedItineraryListAdapter createAdapter() {
        ArrayList<List> list = new ArrayList<>(3);
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> days = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();

        DateFormat dateFormat = DateFormat.getDateInstance();

        if(itineraryList != null) {
            for (Itinerary i : itineraryList) {
                titles.add(i.getTitle());
                days.add(i.getNumberOfDays()+"D");
                dates.add(dateFormat.format(i.getStartDate()));

            }
        } else {
            titles.add("No item in list");
            days.add("");
            dates.add("");
        }

        list.add(titles);
        list.add(days);
        list.add(dates);


        return new SharedItineraryListAdapter(getActivity(), list,itineraryList, new ListItemButtonClickListener());
    }



    private final class ListItemButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = tripList.getFirstVisiblePosition(); i <= tripList.getLastVisiblePosition(); i++) {
                if (v == tripList.getChildAt(i - tripList.getFirstVisiblePosition()).findViewById(R.id.shared_item_card_button_1)) {
                    //View itnernary, transit to the next activity
                    SharedItineraryListFragment.this.viewListItem(i);
                    // PERFORM AN ACTION WITH THE ITEM AT POSITION i
                    //  Toast.makeText(getActivity(), "Clicked on Left Action Button of List Item " + i, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private final class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //Toast.makeText(getActivity(), "Clicked on List Item " + position, Toast.LENGTH_SHORT).show();
        }
    }



}

class SharedItineraryListAdapter extends BaseAdapter {
    private List<String> titleList;
    private List<String> dateList;
    private List<String> daysList;
    private List<Itinerary> itineraryList;


    private final View.OnClickListener itemClickListener;
    private final Context context;

    public SharedItineraryListAdapter(Context context, List<List> lists,List<Itinerary> itineraryList, View.OnClickListener itemClickListener) {
        this.context = context;
        this.titleList = lists.get(0);
        this.daysList = lists.get(1);
        this.dateList = lists.get(2);
        this.itineraryList = itineraryList;
        this.itemClickListener = itemClickListener;
    }

    public List<String> updateList(List<Itinerary> list) {
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> days = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        DateFormat dateFormat = DateFormat.getDateInstance();
        for(Itinerary i : list) {
            titles.add(i.getTitle());
            days.add(i.getNumberOfDays()+"D");
            dates.add(dateFormat.format(i.getStartDate()));
        }
        titleList = titles;
        daysList = days;
        dateList = dates;
        itineraryList = list;
        return titleList;
    }

    @Override
    public String getItem(int position) {
        return titleList.get(position);
    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.shared_list_item, null);

            holder = new ViewHolder();
            holder.itemTitle = (TextView) convertView.findViewById(R.id.shared_tab_list_item_title);
            holder.itemButton1 = (Button) convertView.findViewById(R.id.shared_item_card_button_1);
            holder.itemDate = (TextView) convertView.findViewById(R.id.shared_tab_list_item_date);
            holder.itemDays = (TextView) convertView.findViewById(R.id.shared_tab_list_item_days);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.itemTitle.setText(titleList.get(position));
        holder.itemDate.setText(dateList.get(position));
        holder.itemDays.setText(daysList.get(position));

        if (itemClickListener != null) {
            holder.itemButton1.setOnClickListener(itemClickListener);
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView itemTitle;
        private TextView itemDays;
        private TextView itemDate;
        private Button itemButton1;
    }
}



