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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import edu.purdue.cs.CreateItineraryView;
import edu.purdue.cs.Startup;
import edu.purdue.cs.util.template.TabFragment;
import edu.purdue.cs.util.view.SlidingTabLayout;
import edu.purdue.cs.R;

import java.text.Format;
import java.util.ArrayList;
import java.util.List;

public class SlidingTabsFragment extends Fragment {

    public enum PagerItemTag {TRIP,DISCOVER}




    public class PagerItem {
        private final TabFragment mfragment;
        private final CharSequence mTitle;
        private final PagerItemTag mTag;

        PagerItem(CharSequence title,TabFragment fragment,PagerItemTag tag) {
            mfragment = fragment;
            mTitle = title;
            mTag = tag;

        }


        public Fragment getFragment() {
            return mfragment;
        }

        /**
         * @return the title which represents this tab. In this sample this is used directly by
         * {@link android.support.v4.view.PagerAdapter#getPageTitle(int)}
         */
        CharSequence getTitle() {
            return mTitle;
        }

        PagerItemTag getTag() {return mTag;}
        
    }

    static final String LOG_TAG = "SlidingTabsFragment";

    private SlidingTabLayout mSlidingTabLayout;

    private ViewPager mViewPager;

    /**
     * List of Fragments which will be added to the a.
     */
    private List<PagerItem> mTabs = new ArrayList<PagerItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabs.add(new PagerItem(
                getString(R.string.title_tab_trip),
                new TripTabFragment(),
                PagerItemTag.TRIP
        ));
        mTabs.add(new PagerItem(
                getString(R.string.title_tab_discover),
                new SharedItineraryListFragment(),
                PagerItemTag.DISCOVER
        ));



        //TODO: Add discover fragment to Pager

//        mTabs.add(new PagerItem(
//                getString(R.string.title_tab_discover), // Title
//                null
//        ));

    }

    public PagerItem getPagerItembyTag(PagerItemTag tag) {
        for(PagerItem item : mTabs) {
            Log.d("PagerItem rotation", item.getTitle().toString());
            if(item.getTag().equals(tag)) {
                return item;
            }
        }
        assert(false);
        return null;
    }

    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_startup_fragment, container, false);
    }

    // BEGIN_INCLUDE (fragment_onviewcreated)
    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     *
     * We set the {@link ViewPager}'s adapter to be an instance of
     * {@link AppFragmentPagerAdapter}. The {@link SlidingTabLayout} is then given the
     * {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new AppFragmentPagerAdapter(getChildFragmentManager()));
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);

        // END_INCLUDE (setup_slidingtablayout)
    }
    // END_INCLUDE (fragment_onviewcreated)

    /**
     * The {@link FragmentPagerAdapter} used to display pages in this sample. The individual pages
     * are instances of {@link TripTabFragment} which just display three lines of text. Each page is
     * created by the relevant {@link PagerItem} for the requested position.
     * <p>
     * The important section of this class is the {@link #getPageTitle(int)} method which controls
     * what is displayed in the {@link SlidingTabLayout}.
     */
    class AppFragmentPagerAdapter extends FragmentPagerAdapter {

        AppFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the {@link android.support.v4.app.Fragment} to be displayed at {@code position}.
         * <p>
         * Here we return the value returned from {@link PagerItem#getFragment()}.
         */
        @Override
        public Fragment getItem(int i) {
            return mTabs.get(i).getFragment();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we return the value returned from {@link PagerItem#getTitle()}.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs.get(position).getTitle();
        }
        // END_INCLUDE (pageradapter_getpagetitle)

    }

}