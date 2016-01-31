package edu.purdue.cs;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import edu.purdue.cs.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SplashScreen extends Activity {
    private static int SPLASH_TIME_OUT = 1500;

    private final Class startupClass = Startup.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        final Window window = getWindow();
        final int originalBarColor;
        //this might be redundant
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            originalBarColor = window.getNavigationBarColor();
            window.setNavigationBarColor(ContextCompat.getColor(getApplicationContext(), R.color.app_theme));
        } else {
            originalBarColor =  0;
        }
        Log.d("Splash", "Color" + originalBarColor);



        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over.

                // Change the bar color to fit the app theme.
                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.setNavigationBarColor(originalBarColor);

                // The intent that the app is going to.
                Intent intent;

                ParseUser currentUser = ParseUser.getCurrentUser();

                // Determine whether the current user is logged in
                if (currentUser == null || ParseAnonymousUtils.isLinked(currentUser)) {
                    // The user is a guest. Send the user to LoginActivity.class
                    intent = new Intent(SplashScreen.this, LoginActivity.class);
                    intent.putExtra("SUCCESS_ACTIVITY_CLASS", startupClass);
                } else {
                    // Send logged in users to Startup.class
                    intent = new Intent(SplashScreen.this, startupClass);
                }

                // Redirect the user to the new activity.
                startActivity(intent);

                // Close the current activity.
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
