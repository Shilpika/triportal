package edu.purdue.cs;

import android.util.Log;
import edu.purdue.cs.util.ui.SystemUiHider;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SplashScreen extends Activity {
    private static final int LOGIN_REQUEST = 0;

    private final Class startupClass = Startup.class;
    private  ParseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            // The user is logged in. Direct to the startup activity.
            Log.d("USER ID",currentUser.getUsername());
            Intent intent = new Intent(SplashScreen.this, Startup.class);
            startActivity(intent);
            finish();
        } else {
            // The user is not logged in. Direct to ParseLogin activity.
            ParseLoginBuilder loginBuilder = new ParseLoginBuilder(
                    SplashScreen.this);
            startActivityForResult(loginBuilder.build(), LOGIN_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // The ParseLogin activity has returned.
        if (requestCode == LOGIN_REQUEST) {
            if (resultCode == RESULT_OK) {
                // The user successfully logged in.Direct to the startup activity.
                Intent intent = new Intent(SplashScreen.this, Startup.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
