package com.huebeiro.insuranceco;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.huebeiro.insuranceco.connection.DatabaseHelper;
import com.huebeiro.insuranceco.model.Car;

import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    public static final String KEY_SELECTED_CAR = "key_selected_car";
    public static final String KEY_SELECTED_DATE = "key_selected_date";

    Car currentCar;
    String currentDate;
    TextView lblPlate,lblDayInfo;
    CalendarView mainCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, DayDetailActivity.class);
                intent.putExtra(KEY_SELECTED_CAR, currentCar);
                intent.putExtra(KEY_SELECTED_DATE, currentDate);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mainCalendar = (CalendarView) findViewById(R.id.mainCalendar);
        lblPlate = (TextView) findViewById(R.id.lblPlate);
        lblDayInfo = (TextView) findViewById(R.id.lblDayInfo);

        try {
            currentCar = (Car) getIntent().getSerializableExtra(KEY_SELECTED_CAR);
        } catch (Exception ex){
            Toast.makeText(this,"Couldn't load activity.", Toast.LENGTH_LONG).show();
            finish();
        }

        if(currentCar != null){
            lblPlate.setText(currentCar.getFormattedPlate());
        }
        updateDate();

        mainCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                updateDate();
            }
        });
    }

    private void updateDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mainCalendar.getDate());

        currentDate = getStringDate(calendar);



        DatabaseHelper dbHelper;
        SQLiteDatabase database;
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        int totalEvents = DatabaseHelper.getTotalEvents(database,currentCar,currentDate);
        lblDayInfo.setText(currentDate + " - " + totalEvents + " event(s)");
    }

    private String getStringDate(Calendar calendar){
        return getFormattedDigits(calendar.get(Calendar.DAY_OF_MONTH)) + "/" + getFormattedDigits(calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }

    private String getFormattedDigits(int value){
        String ret = String.valueOf(value);
        if(ret.length() == 1){
            ret = "0" + ret;
        }
        return ret;
    }
}
