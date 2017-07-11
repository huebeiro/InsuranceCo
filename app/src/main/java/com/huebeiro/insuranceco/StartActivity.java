package com.huebeiro.insuranceco;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.huebeiro.insuranceco.adapter.CarList;
import com.huebeiro.insuranceco.connection.DatabaseHelper;
import com.huebeiro.insuranceco.model.Car;

public class StartActivity extends AppCompatActivity {


    Button btnConfirm, btnDelete;
    ListView listCars;
    CarList list;
    Car[] cars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, NewCarActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        listCars = (ListView) findViewById(R.id.listCars);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        refreshList();
        listCars.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listCars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                list.setSelectedIndex(position);
                enableButtons(position != -1);
                listCars.setItemChecked(position, true);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car selCar = cars[list.getSelectedIndex()];
                Intent intent = new Intent(StartActivity.this, CalendarActivity.class);
                intent.putExtra(CalendarActivity.KEY_SELECTED_CAR,selCar);
                startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Car selCar = cars[list.getSelectedIndex()];
                DatabaseHelper dbHelper;
                SQLiteDatabase database;
                dbHelper = new DatabaseHelper(StartActivity.this);
                database = dbHelper.getWritableDatabase();
                database.delete(DatabaseHelper.Car.TABLE_NAME, DatabaseHelper.Car.carID + " = ?", new String[] {String.valueOf(selCar.getId())});
                database.close();
                refreshList();
            }
        });
    }

    private void refreshList(){
        DatabaseHelper dbHelper;
        SQLiteDatabase database;
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        cars = DatabaseHelper.getAllCars(database);
        database.close();

        list = new CarList(this, cars);
        listCars.setAdapter(list);
    }

    private void enableButtons(boolean enable){
        btnDelete.setEnabled(enable);
        btnConfirm.setEnabled(enable);
    }
}
