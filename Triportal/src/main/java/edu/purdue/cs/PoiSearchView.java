package edu.purdue.cs;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseException;

import java.util.List;

import edu.purdue.cs.Adapter.PoiListAdapter;

public class PoiSearchView extends AppCompatActivity implements AdapterView.OnItemClickListener {

    final static int CONFIRM_POI = 1;

    private SearchView mSearchView;
    private ListView mSearchResults;
    private PoiListAdapter mResultsAdapter;
    private int mColIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);

        mColIndex = getIntent().getExtras().getInt("ClickedColumn");
        mSearchView = (SearchView) findViewById(R.id.search_view);
        setupSearchView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        Drawable up = DrawableCompat.wrap(ContextCompat.getDrawable(this, R.drawable.ic_left_arrow));
        DrawableCompat.setTint(up, ContextCompat.getColor(this, R.color.app_body_text_2));
        toolbar.setNavigationIcon(up);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mSearchResults = (ListView) findViewById(R.id.search_results);
        mResultsAdapter = new PoiListAdapter(this);
        mSearchResults.setAdapter(mResultsAdapter);
        mSearchResults.setOnItemClickListener(this);
    }

    private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconified(false);
        // Set the query hint.
        mSearchView.setQueryHint("Search places");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mSearchView.clearFocus();
                searchFor(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                dismiss(null);
                return false;
            }
        });
    }

    private void searchFor(String query) {
        Log.d("search", query);
        List<Poi> poiList;
        try {
            poiList = Poi.search(query);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        mResultsAdapter.swapList(poiList);
        mSearchResults.setVisibility(poiList.size() > 0 ? View.VISIBLE : View.GONE);
    }

    public void dismiss(View view) {
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Poi poi = (Poi)mResultsAdapter.getItem(position);
        try {
            poi.pin();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(PoiSearchView.this, PoiDetailView.class);
        intent.putExtra("poi_id", poi.getObjectId());
        intent.putExtra("edit_mode", PoiDetailView.ADD_MODE);
        startActivityForResult(intent, PoiSearchView.CONFIRM_POI);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PoiSearchView.CONFIRM_POI) {
            if (resultCode == RESULT_OK) {
                String poi_id = data.getStringExtra("poi_id");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("poi_id", poi_id);
                returnIntent.putExtra("ClickedColumn",mColIndex);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        }
    }
}
