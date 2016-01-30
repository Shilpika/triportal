package edu.purdue.cs.util.template;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.purdue.cs.R;

/**
 * Tab Fragment template for startup activity
 */
public class TabFragment extends Fragment {
    //TODO: import information of user etc..
    public static TabFragment newInstance(Bundle bundle) {


        TabFragment fragment = new TabFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: May used to verify user info?
    }

}
