package edu.purdue.cs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class UserProfile extends AppCompatActivity {


    private static void setAvatarInBackground(Bitmap avatar, SaveCallback callback) {
        ByteArrayOutputStream pngOut = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.PNG, 100, pngOut);
        ParseFile file = new ParseFile("avatar.png", pngOut.toByteArray());
        ParseUser user = ParseUser.getCurrentUser();
        user.add("avatar", file);
        user.saveInBackground(callback);
    }

    private static Bitmap getAvatar() throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        ParseFile file = user.getParseFile("avatar");
        ByteArrayInputStream pngIn = new ByteArrayInputStream(file.getData());
        Bitmap avatar = BitmapFactory.decodeStream(pngIn);
        return avatar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar action = (Toolbar)findViewById(R.id.actionBar);
        setSupportActionBar(action);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        ParseUser user = ParseUser.getCurrentUser();
        //String username = user.getEmail();

        String username = user.getString("name");

        action.setTitle(username);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent back = new Intent(UserProfile.this, SplashScreen.class);
            startActivity(back);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
