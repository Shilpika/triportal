package edu.purdue.cs.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.parse.FindCallback;
import com.parse.ParseException;
import edu.purdue.cs.Day;
import edu.purdue.cs.Listener.onFinishedListener;
import edu.purdue.cs.Poi;

import java.util.List;

/**
 * Created by Bowie on 2016/4/28.
 */
public class GoogleMapsUtil {
    final static private String URI_PRE = "https://www.google.com/maps/dir/";

    public static void parseDayInOrder(Day day, final onFinishedListener callback) {
        day.getPoiListInBackground(new FindCallback<Poi>() {
            @Override
            public void done(List<Poi> objects, ParseException e) {
                if(objects != null) {
                    callback.onFinishedParsed(_buildNavigationUrl(objects));
                }
            }
        });
    }

    public static void intentApp(String uriString, Context context){
        Uri uri = Uri.parse(uriString);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }
    private static String _buildNavigationUrl(List<Poi> poiList) {
        String uriBuilder = URI_PRE;
        for(Poi poi : poiList) {
            uriBuilder += poi.getName().replace(' ','+');
            uriBuilder += '/';
            uriBuilder += '+' + poi.getLocationString();
            uriBuilder += '/';
        }
        return  uriBuilder;
    }


}
