package com.huebeiro.insuranceco;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.huebeiro.insuranceco.model.Attachment;

public class AddAttachmentActivity extends AppCompatActivity {

    EditText txtDescription;
    ImageView imgAttach;
    ImageButton addImage;
    Attachment attachment;
    Button btnSave, btnCancel;

    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_FROM_GALLERY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attachment);

        txtDescription = (EditText) findViewById(R.id.txtDescription);
        addImage = (ImageButton) findViewById(R.id.btnAdd);
        imgAttach = (ImageView) findViewById(R.id.imgAttach);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnCancel = (Button) findViewById(R.id.btnCancel);


        final String[] option = new String[] { "Take from Camera",
                "Select from Gallery" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, option);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Select Option");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Log.e("Selected Item", String.valueOf(which));
                if (which == 0) {
                    callCamera();
                }
                if (which == 1) {
                    callGallery();
                }

            }
        });
        final AlertDialog dialog = builder.create();

        addImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.show();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(attachment != null && attachment.getFile() != null){
                    if(txtDescription.getText().length() > 0){
                        attachment.setDescription(txtDescription.getText().toString());
                        NewEventActivity.addAttachment(attachment);
                        finish();
                    } else {
                        Toast.makeText(AddAttachmentActivity.this,"Add a description to the image before saving.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(AddAttachmentActivity.this,"Select image or take photo before saving.", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setImageContent(byte[] content){
        Bitmap bitmap = BitmapFactory.decodeByteArray(content, 0,
                content.length);
        imgAttach.setImageBitmap(bitmap);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case CAMERA_REQUEST:

                Bundle extras = data.getExtras();

                if (extras != null) {
                    Bitmap yourImage = extras.getParcelable("data");
                    // convert bitmap to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte imageInByte[] = stream.toByteArray();
                    Log.e("AddAttachmentActivity", imageInByte.toString());
                    // Inserting Contacts
                    Log.d("Insert: ", "Inserting ..");

                    attachment = new Attachment();
                    attachment.setFile(imageInByte);
                    setImageContent(imageInByte);
                }
                break;
            case PICK_FROM_GALLERY:
                Bundle extras2 = data.getExtras();

                if (extras2 != null) {
                    Bitmap yourImage = extras2.getParcelable("data");
                    // convert bitmap to byte
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte imageInByte[] = stream.toByteArray();
                    Log.e("AddAttachmentActivity", imageInByte.toString());
                    // Inserting Contacts
                    Log.d("Insert: ", "Inserting ..");
                    attachment = new Attachment();
                    attachment.setFile(imageInByte);
                    setImageContent(imageInByte);
                }

                break;
        }
    }

    /**
     * open camera method
     */
    public void callCamera() {
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra("aspectX", 0);
        cameraIntent.putExtra("aspectY", 0);
        cameraIntent.putExtra("outputX", 200);
        cameraIntent.putExtra("outputY", 150);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    /**
     * open gallery method
     */

    public void callGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(
                Intent.createChooser(intent, "Complete action using"),
                PICK_FROM_GALLERY);

    }

}

