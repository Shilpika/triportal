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

                // Determine whether the current user is an anonymous user
                if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                    // If user is anonymous, send the user to LoginActivity.class
                    intent = new Intent(SplashScreen.this, LoginActivity.class);
                } else {
                    // If current user is NOT anonymous user
                    // Get current user data from Parse.com
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        // Send logged in users to Startup.class
                        intent = new Intent(SplashScreen.this, Startup.class);
                    } else {
                        // Send user to LoginActivity.class
                        intent = new Intent(SplashScreen.this, LoginActivity.class);
                    }
                }

                // Redirect the user to the new activity.
                startActivity(intent);

                // Close the current activity.
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
