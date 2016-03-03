package edu.purdue.cs;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;

import android.content.Context;
import android.support.multidex.MultiDex;


public class Triportal extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Initialize Parse support.
        Parse.initialize(this);
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        // Setup default access control on user created data.
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);

        // Initialize ParseFacebook support.
        ParseFacebookUtils.initialize(this);

        // Initialize ParseTwitter support.
        ParseTwitterUtils.initialize(getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret));

        ParseObject.registerSubclass(Itinerary.class);
        ParseObject.registerSubclass(Poi.class);
        ParseObject.registerSubclass(Day.class);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
