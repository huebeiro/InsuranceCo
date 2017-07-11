package com.huebeiro.insuranceco;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.huebeiro.insuranceco.connection.DatabaseHelper;
import com.huebeiro.insuranceco.connection.HttpRequestBrands;
import com.huebeiro.insuranceco.connection.HttpRequestModels;
import com.huebeiro.insuranceco.model.Brand;
import com.huebeiro.insuranceco.model.Model;

import java.util.ArrayList;
import java.util.List;

public class NewCarActivity extends AppCompatActivity {

    //Declaring widgets
    Spinner spnBrands, spnModels;
    Button btnSave, btnCancel;
    EditText txtPlate1, txtPlate2, txtYear;

    //The collection of car info for the spinners
    Brand[] brands;
    Model[] models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);
    }

    @Override
    protected void onStart() {
        super.onStart();

        spnBrands = (Spinner) findViewById(R.id.spnBrand);
        spnModels = (Spinner) findViewById(R.id.spnModel);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        txtPlate1 = (EditText) findViewById(R.id.txtPlate1);
        txtPlate2 = (EditText) findViewById(R.id.txtPlate2);
        txtYear = (EditText) findViewById(R.id.txtYear);


        spnBrands.setEnabled(false);
        spnModels.setEnabled(false);

        spnBrands.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                HttpRequestModels task = new HttpRequestModels() {
                    @Override
                    protected void onPostExecute(Model[] models) {
                        super.onPostExecute(models);
                        NewCarActivity.this.models = models;
                        if (models != null) {
                            List<String> spinnerArray = new ArrayList<String>();
                            for (Model model : models) {
                                spinnerArray.add(model.getFipe_name());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    NewCarActivity.this, android.R.layout.simple_spinner_item, spinnerArray);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spnModels.setAdapter(adapter);
                            spnModels.setEnabled(true);
                        } else {
                            //Snackbar.make()
                        }
                    }
                };

                int brandId = brands[position].getId();
                task.execute(brandId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {

                    Brand selBrand = brands[spnBrands.getSelectedItemPosition()];
                    Model selModel = models[spnModels.getSelectedItemPosition()];

                    DatabaseHelper dbHelper;
                    SQLiteDatabase database;
                    dbHelper = new DatabaseHelper(NewCarActivity.this);
                    database = dbHelper.getWritableDatabase();

                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.Car.carAno, txtYear.getText().toString());
                    values.put(DatabaseHelper.Car.carLicencePlate, txtPlate1.getText().toString().toUpperCase() + txtPlate2.getText().toString());
                    values.put(DatabaseHelper.Car.carFipeID, selModel.getId());
                    values.put(DatabaseHelper.Car.carMarca, selModel.getFipe_marca());
                    values.put(DatabaseHelper.Car.carModelo, selModel.getFipe_name());

                    database.insert(DatabaseHelper.Car.TABLE_NAME, null, values);

                    database.close();
                    finish();
                } else {
                    //TODO Snackbar
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        HttpRequestBrands task = new HttpRequestBrands() {
            @Override
            protected void onPostExecute(Brand[] brands) {
                super.onPostExecute(brands);
                NewCarActivity.this.brands = brands;
                if(brands != null) {
                    List<String> spinnerArray =  new ArrayList<String>();
                    for(Brand brand : brands){
                        spinnerArray.add(brand.getFipe_name());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            NewCarActivity.this, android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnBrands.setAdapter(adapter);
                    spnBrands.setEnabled(true);
                } else {
                    //Snackbar.make()
                }
            }
        };
        task.execute();





    }


    public boolean validateFields(){
        boolean valid = true;
        if(txtPlate1.getText().length() != 3)
            valid = false;
        if(txtPlate2.getText().length() != 4)
            valid = false;
        if(txtYear.getText().length() != 4)
            valid = false;
        return valid;
    }
}
