package edu.purdue.cs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CreateItineraryView extends AppCompatActivity {

    int y, m, d;
    static final int D_id = 0;

    Button bt;
    EditText StartDate;
    EditText title;
    Date sDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_itinerary_view);
        StartDate = (EditText)findViewById(R.id.StartDate);

        final Calendar cal = Calendar.getInstance();
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);

        showDialogOnClick();

        title = (EditText)findViewById(R.id.title);
        Toolbar actionbar = (Toolbar) findViewById(R.id.BarTitle);
        setSupportActionBar(actionbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionbar.setTitle("Create New Itinerary");

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create_finish) {

            Itinerary itinerary = Itinerary.create();
            if(title.getText().length() <= 0 || sDate == null){
                Toast.makeText(getApplicationContext(), "Invaild input", Toast.LENGTH_LONG).show();
                return true;
            }
            itinerary.setTitle(title.getText().toString());
            itinerary.setStartDate(sDate);
            itinerary.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    Log.d("finish", "Finish");
                    finish();
                }
            });
            return true;
        }
        return false;
    }




    public void showDialogOnClick(){
        bt = (Button)findViewById(R.id.setDate);

        bt.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        showDialog(D_id);
                    }
                }
        );
    }
    @Override
    protected Dialog onCreateDialog(int id){
        if(id == D_id)
            return new DatePickerDialog(this, dpickerListener, y,m,d);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
            y = year;
            m = monthOfYear + 1;
            d = dayOfMonth;
            Calendar cal = Calendar.getInstance();
            cal.set(y, m, d);
            sDate = cal.getTime();
            StartDate.setText(m + "/" + d + "/" + y);
        }
    };


}
