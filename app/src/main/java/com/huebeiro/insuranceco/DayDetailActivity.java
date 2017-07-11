package com.huebeiro.insuranceco;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.huebeiro.insuranceco.R;
import com.huebeiro.insuranceco.adapter.EventList;
import com.huebeiro.insuranceco.connection.DatabaseHelper;
import com.huebeiro.insuranceco.model.Car;
import com.huebeiro.insuranceco.model.Event;

public class DayDetailActivity extends AppCompatActivity {

    Car currentCar;
    String currentDate;

    ListView listEvents;
    TextView lblDate;
    Button btnSelect, btnCancel;
    Event[] events;
    EventList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DayDetailActivity.this,NewEventActivity.class);
                intent.putExtra(CalendarActivity.KEY_SELECTED_CAR, currentCar);
                intent.putExtra(CalendarActivity.KEY_SELECTED_DATE, currentDate);
                startActivity(intent);
            }
        });

        currentCar = (Car) getIntent().getSerializableExtra(CalendarActivity.KEY_SELECTED_CAR);
        currentDate = getIntent().getStringExtra(CalendarActivity.KEY_SELECTED_DATE);

        listEvents = (ListView) findViewById(R.id.listEvents);
        lblDate = (TextView) findViewById(R.id.txtDetailDate);
        btnSelect = (Button) findViewById(R.id.btnSelect);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DayDetailActivity.this,ViewEventActivity.class);
                intent.putExtra(CalendarActivity.KEY_SELECTED_CAR, currentCar);
                intent.putExtra(CalendarActivity.KEY_SELECTED_DATE, currentDate);
                intent.putExtra(ViewEventActivity.KEY_SELECTED_EVENT, events[list.getSelectedIndex()]);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        lblDate.setText(currentDate);
        updateListView();
    }

    public void updateListView(){
        DatabaseHelper dbHelper;
        SQLiteDatabase database;
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        btnSelect.setEnabled(false);
        events = DatabaseHelper.getEvents(database, currentCar, currentDate);
        list = new EventList(this, events);
        listEvents.setAdapter(list);
        listEvents.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                list.setSelectedIndex(position);
                btnSelect.setEnabled(position != -1);
                listEvents.setItemChecked(position, true);
            }
        });
    }

}
