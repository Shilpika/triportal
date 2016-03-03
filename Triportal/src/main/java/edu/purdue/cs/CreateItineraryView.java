package edu.purdue.cs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class CreateItineraryView extends AppCompatActivity {

    int y, m, d;
    static final int D_id = 0;

    Button bt;
    EditText StartDate;
    EditText title;
    String ItineraryTitle;

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
        ItineraryTitle = title.getText().toString();
        Toolbar actionbar = (Toolbar) findViewById(R.id.BarTitle);
        setSupportActionBar(actionbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionbar.setTitle("Create New Itinerary");
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
            StartDate.setText(m + "/" + d + "/" + y);
        }
    };


}
