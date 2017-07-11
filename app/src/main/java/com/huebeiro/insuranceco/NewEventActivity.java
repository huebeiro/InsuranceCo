package com.huebeiro.insuranceco;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.huebeiro.insuranceco.adapter.AttachList;
import com.huebeiro.insuranceco.adapter.CauseList;
import com.huebeiro.insuranceco.connection.DatabaseHelper;
import com.huebeiro.insuranceco.model.Attachment;
import com.huebeiro.insuranceco.model.Car;
import com.huebeiro.insuranceco.model.Cause;

import java.util.ArrayList;
import java.util.List;

public class NewEventActivity extends AppCompatActivity {

    Car currentCar;
    String currentDate;

    EditText txtPlate1, txtPlate2, txtDate;
    ImageButton imgAddCause, imgAddAttach;
    Button btnSave, btnCancel;
    ListView lvAttach,lvCauses;

    private static boolean[] listCauses;
    private static ArrayList<Attachment> listAttach;

    public static void addCause(int cause){
        if(listCauses == null)
            startListCauses();
        listCauses[cause] = true;
    }

    private static void startListCauses(){
        listCauses = new boolean[AddCauseActivity.causes.length];
    }

    private static Cause[] getArrayCauses(){
        ArrayList<Cause> aCauses = new ArrayList<>();
        for(int pos = 1; pos < listCauses.length; pos++){
            if(listCauses[pos]){
                Cause cause = new Cause();
                cause.setDescription(AddCauseActivity.causes[pos]);
                cause.setId(pos);
                aCauses.add(cause);
            }
        }
        Cause[] causes = new Cause[aCauses.size()];
        causes = aCauses.toArray(causes);
        return causes;
    }

    public static void addAttachment(Attachment attachment){
        if(listAttach == null)
            startListCauses();
        listAttach.add(attachment);
    }

    private static void startListAttach(){
        listAttach = new ArrayList<>();
    }

    private static Attachment[] getArrayAttachments(){
        Attachment[] attachs = new Attachment[listAttach.size()];
        attachs = listAttach.toArray(attachs);
        return attachs;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        startListCauses();
        startListAttach();

        currentCar = (Car) getIntent().getSerializableExtra(CalendarActivity.KEY_SELECTED_CAR);
        currentDate = getIntent().getStringExtra(CalendarActivity.KEY_SELECTED_DATE);

        txtPlate1 = (EditText) findViewById(R.id.txtPlate1);
        txtPlate2 = (EditText) findViewById(R.id.txtPlate2);
        txtDate = (EditText) findViewById(R.id.txtDate);

        imgAddCause = (ImageButton) findViewById(R.id.btnAddCause);
        imgAddAttach = (ImageButton) findViewById(R.id.btnAddAttach);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSave = (Button) findViewById(R.id.btnSave);

        lvAttach = (ListView) findViewById(R.id.listAttach);
        lvCauses = (ListView) findViewById(R.id.listCauses);

        String[] plate = currentCar.getFormattedPlate().split("-");

        txtPlate1.setText(plate[0]);
        txtPlate2.setText(plate[1]);
        txtDate.setText(currentDate);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvent();
                finish();
            }
        });
        imgAddCause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewEventActivity.this, AddCauseActivity.class);
                startActivity(intent);
            }
        });
        imgAddAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewEventActivity.this, AddAttachmentActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Prevent keyboard from showing at onStart
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        updateListViews();
    }

    public void updateListViews(){
        CauseList causeList = new CauseList(this, getArrayCauses()){
            @Override
            public void itemClick(Cause cause) {
                super.itemClick(cause);
                listCauses[cause.getId()] = false;
                updateListViews();
            }
        };
        lvCauses.setAdapter(causeList);

        AttachList attachList = new AttachList(this,getArrayAttachments()){
            @Override
            public void itemClick(Attachment attachment) {
                super.itemClick(attachment);
                listAttach.remove(attachment);
                updateListViews();
            }
        };
        lvAttach.setAdapter(attachList);
    }

    public void saveEvent(){
        DatabaseHelper dbHelper;
        SQLiteDatabase database;
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.Event.carID, currentCar.getId());
        values.put(DatabaseHelper.Event.eveDate, currentDate);

        int id = (int) database.insert(DatabaseHelper.Event.TABLE_NAME, null, values);

        if(id != -1) {
            for (Cause cause : getArrayCauses()) {
                ContentValues valuesCause = new ContentValues();
                valuesCause.put(DatabaseHelper.EventRelCause.eveID, id);
                valuesCause.put(DatabaseHelper.EventRelCause.relID, cause.getId());
                database.insert(DatabaseHelper.EventRelCause.TABLE_NAME, null, valuesCause);
            }

            for (Attachment attachment : getArrayAttachments()) {
                ContentValues valuesCause = new ContentValues();
                valuesCause.put(DatabaseHelper.EventAttach.eveID, id);
                valuesCause.put(DatabaseHelper.EventAttach.evaDescription, attachment.getDescription());
                valuesCause.put(DatabaseHelper.EventAttach.evaFile, attachment.getFile());
                database.insert(DatabaseHelper.EventAttach.TABLE_NAME, null, valuesCause);
            }

        } else {
            Toast.makeText(this,"It wasn't possible to save the event.", Toast.LENGTH_SHORT).show();
        }
        database.close();
    }
}
