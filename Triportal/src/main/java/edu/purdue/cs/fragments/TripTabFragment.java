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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import edu.purdue.cs.util.template.TabFragment;

import edu.purdue.cs.R;

import java.util.ArrayList;
import java.util.List;


public class TripTabFragment extends TabFragment {

    /**
     * inflating view of tab contents
     */

    private ListView tripList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.trip_tab, container, false);
        //inflating the card-style list
        tripList = (ListView) rootView.findViewById(R.id.trip_list);
        setupList();

        return rootView;

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
        tripList.setAdapter(createAdapter());
        tripList.setOnItemClickListener(new ListItemClickListener());
    }

    private TripListAdapter createAdapter() {
        ArrayList<String> items = new ArrayList<String>();

        for (int i = 0; i < 100; i++) {
            items.add(i, "Image for List Item " + i);
        }

        return new TripListAdapter(getActivity(), items, new ListItemButtonClickListener());
    }

    private final class ListItemButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = tripList.getFirstVisiblePosition(); i <= tripList.getLastVisiblePosition(); i++) {
                if (v == tripList.getChildAt(i - tripList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_button_1)) {
                    // PERFORM AN ACTION WITH THE ITEM AT POSITION i
                  //  Toast.makeText(getActivity(), "Clicked on Left Action Button of List Item " + i, Toast.LENGTH_SHORT).show();
                } else if (v == tripList.getChildAt(i - tripList.getFirstVisiblePosition()).findViewById(R.id.list_item_card_button_2)) {
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
    private List<String> items;
    private final View.OnClickListener itemClickListener;
    private final Context context;

    public TripListAdapter(Context context, List<String> items, View.OnClickListener itemClickListener) {
        this.context = context;
        this.items = items;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public String getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
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

        holder.itemText.setText(items.get(position));

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



