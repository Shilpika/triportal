/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package edu.purdue.cs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseAnalytics;


public class Startup extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_startup);
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
    Toolbar actionbar = (Toolbar) findViewById(R.id.startupActionBar);
    setSupportActionBar(actionbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    //TODO: Turn this into ViewPage, The contents will be in tabs

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_starup, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_user_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }


  public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {
    public AppSectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int i) {
      switch (i) {
        case 0:
          //TODO
          return null;

        default:
          return null;
      }
    }

    @Override
    public int getCount() {
      return 2; //number of tabs will be added
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return "Section " + (position + 1);
    }
  }
}

