package com.huebeiro.insuranceco;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huebeiro.insuranceco.adapter.AttachList;
import com.huebeiro.insuranceco.adapter.CauseList;
import com.huebeiro.insuranceco.connection.DatabaseHelper;
import com.huebeiro.insuranceco.model.Attachment;
import com.huebeiro.insuranceco.model.Car;
import com.huebeiro.insuranceco.model.Cause;
import com.huebeiro.insuranceco.model.Event;

public class ViewEventActivity extends AppCompatActivity {

    public static final String KEY_SELECTED_EVENT = "key_seleted_event";

    Car currentCar;
    Event currentEvent;
    String currentDate;

    Cause[] causes;
    Attachment[] attaches;

    CauseList listCause;
    AttachList listAttach;

    EditText txtPlate1, txtPlate2, txtDate;
    Button btnDelete, btnCancel;
    ListView lvAttach,lvCauses;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        txtPlate1 = (EditText) findViewById(R.id.txtPlate1);
        txtPlate2 = (EditText) findViewById(R.id.txtPlate2);
        txtDate = (EditText) findViewById(R.id.txtDate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        lvAttach = (ListView) findViewById(R.id.listAttach);
        lvCauses = (ListView) findViewById(R.id.listCauses);

        currentCar = (Car) getIntent().getSerializableExtra(CalendarActivity.KEY_SELECTED_CAR);
        currentDate = getIntent().getStringExtra(CalendarActivity.KEY_SELECTED_DATE);
        currentEvent = (Event) getIntent().getSerializableExtra(KEY_SELECTED_EVENT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Prevent keyboard from showing at onStart
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        String[] plate = currentCar.getFormattedPlate().split("-");

        txtPlate1.setText(plate[0]);
        txtPlate2.setText(plate[1]);
        txtDate.setText(currentDate);
        updateLists();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper;
                SQLiteDatabase database;
                dbHelper = new DatabaseHelper(ViewEventActivity.this);
                database = dbHelper.getWritableDatabase();

                database.delete(DatabaseHelper.EventAttach.TABLE_NAME,DatabaseHelper.EventAttach.eveID + " = " + currentEvent.getId(),null);
                database.delete(DatabaseHelper.EventRelCause.TABLE_NAME,DatabaseHelper.EventRelCause.eveID + " = " + currentEvent.getId(),null);
                database.delete(DatabaseHelper.Event.TABLE_NAME,DatabaseHelper.Event.eveID + " = " + currentEvent.getId(),null);

                database.close();
                finish();
            }
        });
    }

    private void updateLists(){
        DatabaseHelper dbHelper;
        SQLiteDatabase database;
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        causes = DatabaseHelper.getCauses(database, currentEvent);

        listCause = new CauseList(this, causes){
            @Override
            public void itemClick(final Cause cause) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewEventActivity.this);
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.alert_fragment, null);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface d) {
                        ((ImageView) dialog.findViewById(R.id.imageAlert)).setImageResource(AddCauseActivity.images[cause.getId()]);
                        ((TextView) dialog.findViewById(R.id.txtAlert)).setText(cause.getDescription());
                    }
                });

                dialog.show();
            }
        };
        listCause.setImageRes(R.drawable.ic_details);
        lvCauses.setAdapter(listCause);

        attaches = DatabaseHelper.getAttachments(database, currentEvent);
        listAttach = new AttachList(this, attaches){
            @Override
            public void itemClick(final Attachment attachment) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewEventActivity.this);
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                final AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.alert_fragment, null);
                dialog.setView(dialogLayout);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface d) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(attachment.getFile(), 0,
                                attachment.getFile().length);
                        ((ImageView) dialog.findViewById(R.id.imageAlert)).setImageBitmap(bitmap);
                        ((TextView) dialog.findViewById(R.id.txtAlert)).setText(attachment.getDescription());
                    }
                });

                dialog.show();
            }
        };
        listAttach.setImageRes(R.drawable.ic_details);
        lvAttach.setAdapter(listAttach);

        database.close();
    }
}
