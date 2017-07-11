package com.huebeiro.insuranceco;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AddCauseActivity extends AppCompatActivity {

    public static String[] causes = new String[]{
            "Select a cause...",
            "Direction System",
            "Doors",
            "Engine",
            "Gear Box",
            "Radiator",
            "Suspension System",
            "Tires",
            "Wheels"
    };

    public static int[] images = {
            R.drawable.img_car_default,
            R.drawable.img_car_direction,
            R.drawable.img_car_doors,
            R.drawable.img_car_engine,
            R.drawable.img_car_gears,
            R.drawable.img_car_radiator,
            R.drawable.img_car_suspension,
            R.drawable.img_car_tires,
            R.drawable.img_car_wheels
    };

    private int currentCause = 0;

    ImageView imgParts;
    ImageButton imgBack, imgForward;
    Button btnSave, btnCancel;
    TextView lblCause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cause);
    }

    @Override
    protected void onStart() {
        super.onStart();

        imgParts = (ImageView) findViewById(R.id.imgPart);
        imgBack = (ImageButton) findViewById(R.id.imgBack);
        imgForward = (ImageButton) findViewById(R.id.imgForward);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        lblCause = (TextView) findViewById(R.id.lblCause);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCause--;
                updateView();
            }
        });
        imgForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCause++;
                updateView();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentCause != 0) {
                    NewEventActivity.addCause(currentCause);
                    finish();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        updateView();
    }

    private void updateView(){
        if(currentCause == -1)
            currentCause = causes.length -1;
        if(currentCause == causes.length)
            currentCause = 0;

        imgParts.setImageResource(images[currentCause]);
        lblCause.setText(causes[currentCause]);
        btnSave.setEnabled(currentCause != 0);
    }
}
