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
import android.support.v4.app.Fragment;
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

import edu.purdue.cs.util.view.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;


public class TripTabFragment extends TabFragment {

    /**
     * inflating view of tab contents
     */

    private ListView tripList;
    private ImageButton createButton;
    private List<Itinerary> itineraryList;
    private Context pContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.trip_tab, container, false);
        //inflating the card-style list
        tripList = (ListView) rootView.findViewById(R.id.trip_list);
        createButton = (ImageButton) rootView.findViewById(R.id.trip_tab_create_i_btn);
        setupList();
        setupButton();



        return rootView;

    }

    public void setupButton() {

        /**
         * set up create button(s), this could be from various tabs, currently only from the trip tab
         */

        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Link crate button to the CreateItineraryView
                Intent i = new Intent(getActivity(),CreateItineraryView.class);
                getActivity().startActivity(i);
               // getActivity().finish();
            }
        });
    }




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
        Itinerary.getMyItineraryListInBackground(new FindCallback<Itinerary>() {
            @Override
            public void done(List<Itinerary> objects, ParseException e) {
                itineraryList = objects;
            }
        });
       // assert(itineraryList.size() != 0);
        tripList.setAdapter(createAdapter());
        tripList.setOnItemClickListener(new ListItemClickListener());

    }

    private void refreshList() {
        Itinerary.getMyItineraryListInBackground(new FindCallback<Itinerary>() {
            @Override
            public void done(List<Itinerary> objects, ParseException e) {
                itineraryList = objects;
                TripListAdapter adapter = (TripListAdapter) tripList.getAdapter();
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

    private void deleteListItem(int index) throws ParseException {
        // this should be working since the order of the List View is base on the ItneraryList we fetch from server
        Itinerary itinerary = itineraryList.get(index);
        // make sure this itinerary is pin in local storage
        itinerary.deleteMyItineraryListEventually(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                Toast.makeText(TripTabFragment.this.getActivity(), "Delete Success" , Toast.LENGTH_SHORT).show();
                TripTabFragment.this.refreshList();
            }
        });

    }

    private TripListAdapter createAdapter() {
        ArrayList<String> items = new ArrayList<String>();

//        for (int i = 0; i < 100; i++) {
//            items.add(i, "Image for List Item " + i);
//        }
        if(itineraryList != null) {
            for (Itinerary i : itineraryList) {
                items.add(i.getTitle());
            }
        } else {
            items.add("No item in list");
        }

        return new TripListAdapter(getActivity(), items,itineraryList, new ListItemButtonClickListener());
    }



    private final class ListItemButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = tripList.getFirstVisiblePosition(); i <= tripList.getLastVisiblePosition(); i++) {
                if (v == tripList.getChildAt(i - tripList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_button_1)) {
                    //View itnernary, transit to the next activity
                    TripTabFragment.this.viewListItem(i);
                    // PERFORM AN ACTION WITH THE ITEM AT POSITION i
                  //  Toast.makeText(getActivity(), "Clicked on Left Action Button of List Item " + i, Toast.LENGTH_SHORT).show();
                } else if (v == tripList.getChildAt(i - tripList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_button_2)) {
                    //Delete Itnerary, delete on server
                    try {
                        TripTabFragment.this.deleteListItem(i);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    // PERFORM ANOTHER ACTION WITH THE ITEM AT POSITION i
                   // Toast.makeText(getActivity(), "Clicked on Right Action Button of List Item " + i, Toast.LENGTH_SHORT).show();
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

class TripListAdapter extends BaseAdapter {
    private List<String> titleList;
    private List<Itinerary> itineraryList;

    private final View.OnClickListener itemClickListener;
    private final Context context;

    public TripListAdapter(Context context, List<String> items,List<Itinerary> itineraryList, View.OnClickListener itemClickListener) {
        this.context = context;
        this.titleList = items;
        this.itineraryList = itineraryList;
        this.itemClickListener = itemClickListener;
    }

    public List<String> updateList(List<Itinerary> list) {
        ArrayList<String> newList = new ArrayList<>();
        for(Itinerary i : list) {
            newList.add(i.getTitle());
        }
        titleList = newList;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.trip_tab_list_item, null);

            holder = new ViewHolder();
            holder.itemText = (TextView) convertView.findViewById(R.id.trip_tab_list_item_text);
            holder.itemButton1 = (Button) convertView.findViewById(R.id.list_item_card_button_1);
            holder.itemButton2 = (Button) convertView.findViewById(R.id.list_item_card_button_2);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.itemText.setText(titleList.get(position));

        if (itemClickListener != null) {
            holder.itemButton1.setOnClickListener(itemClickListener);
            holder.itemButton2.setOnClickListener(itemClickListener);
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView itemText;
        private Button itemButton1;
        private Button itemButton2;
    }
}



