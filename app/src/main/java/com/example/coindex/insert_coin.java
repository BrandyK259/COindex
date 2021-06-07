package com.example.coindex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class insert_coin extends AppCompatActivity {

    //variables for the camera controls
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    int coinFace = 0;

    Button hCaptureBtn;
    ImageView himageView;

    Uri himage_uri;

    Button tCaptureBtn;
    ImageView timageView;

    Uri timage_uri;

    //variables for the database controls
    EditText info, type, quantity;
    Button save;
    databaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initial code
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_coin);

        //code for camera controls
        himageView = findViewById(R.id.image_view_heads);
        hCaptureBtn = findViewById(R.id.capture_image_heads);

        hCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if system os is >= marshmallow, then get permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    coinFace = 1;
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not already granted, so get it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //pop-up for permission request
                        requestPermissions(permission, PERMISSION_CODE);

                    }
                    else {
                        //already have permission
                        openCamera();

                    }
                }
                else{
                    //system os < marshmallow
                    openCamera();

                }
            }

        });

        timageView = findViewById(R.id.image_view_tails);
        tCaptureBtn = findViewById(R.id.capture_image_tails);

        tCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coinFace = 2;
                //if system os is >= marshmallow, then get permissions
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                        //permission not already granted, so get it
                        String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        //pop-up for permission request
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                    else {
                        //already have permission
                        openCamera();
                    }
                }
                else{
                    //system os < marshmallow
                    openCamera();
                }
            }

        });

        //database control code now
        info = findViewById(R.id.info_text);
        type = findViewById(R.id.type_text);
        quantity = findViewById(R.id.quantity_text);

        save = findViewById(R.id.btn_save);
        DB = new databaseHelper(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_info = info.getText().toString();
                String text_type = type.getText().toString();
                String text_quantity = quantity.getText().toString();

                Boolean check_save_data = DB.save_coin_data(text_info,text_type,text_quantity);

                if (check_save_data == true){
                    Toast.makeText(insert_coin.this, "New Coin(s) Saved",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(insert_coin.this, "New Coin(s) Not Saved",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void gotoMain (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put (MediaStore.Images.Media.TITLE, "New Picture");
        values.put (MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        if (coinFace == 1) {
            himage_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
        else if(coinFace == 2){
            timage_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

        //create camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (coinFace == 1){
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, himage_uri);
        }
        else if(coinFace == 2){
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, timage_uri);
        }
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    //handle permission from pop-up
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission from pop-up = granted
                openCamera();
            } else {
                //permission from pop-up = denied
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //gets called when image is captured from camera

       super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && coinFace == 1) {
            himageView.setImageURI(himage_uri);
        }
        else if(resultCode == RESULT_OK && coinFace == 2){
            timageView.setImageURI(timage_uri);
        }
    }
}