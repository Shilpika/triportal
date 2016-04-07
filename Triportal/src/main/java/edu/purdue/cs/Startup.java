package edu.purdue.cs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;

import java.util.List;

import edu.purdue.cs.fragments.SlidingTabsFragment;
import edu.purdue.cs.fragments.TripTabFragment;
import edu.purdue.cs.util.view.SlidingTabLayout;


public class Startup extends AppCompatActivity {

    private SlidingTabsFragment StartupTabFragmentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        this.StartupTabFragmentView = (SlidingTabsFragment) getSupportFragmentManager().findFragmentById(R.id.activity_startup_fragment);
        /**
         * set up toolbar
         */
        Toolbar actionbar = (Toolbar) findViewById(R.id.startupActionBar);
        setSupportActionBar(actionbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        assert (StartupTabFragmentView != null);

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
          Intent user = new Intent(Startup.this, UserProfile.class);
          startActivity(user);
          //finish();
          return true;
      }
      return false;
  }

}

