package com.example.coindex;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class insert_coin extends AppCompatActivity {

    //variables for the camera controls
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    int coinFace = 0;

    // create the path for external storage
    File path = Environment.getExternalStorageDirectory();
    // create the folder for the images to be saved in

    Button hCaptureBtn;
    ImageView himageView;

    Uri himage_uri;
    Uri timage_uri;

    Button tCaptureBtn;
    ImageView timageView;

    Bitmap h_bitmap;
    Bitmap t_bitmap;

    File h_file;
    File t_file;

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
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {


                        //permission not already granted, so get it
                        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                            //all permissions need to be granted
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                            //pop-up for permission request
                            requestPermissions(permission, PERMISSION_CODE);
                        } else if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                            //permission to write to storage and camera use is needed
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        } else if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                            //permission to read from storage and camera use is needed
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                            //permission to read/write from/to storage is needed
                            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        } else if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            //only permission to read from storage is needed
                            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            //only permission for camera use is needed
                            String[] permission = {Manifest.permission.CAMERA};
                            requestPermissions(permission, PERMISSION_CODE);
                        } else {
                            //only permission to write to storage is needed
                            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        }

                    } else {
                        //already have permission
                        openCamera();
                    }
                } else {
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
                            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                        //permission not already granted, so get it
                        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                            //all permissions need to be granted
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                            //pop-up for permission request
                            requestPermissions(permission, PERMISSION_CODE);
                        } else if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                            //permission to write to storage and camera use is needed
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        } else if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                            //permission to read from storage and camera use is needed
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                            //permission to read/write from/to storage is needed
                            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        } else if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            //only permission to read from storage is needed
                            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            //only permission for camera use is needed
                            String[] permission = {Manifest.permission.CAMERA};
                            requestPermissions(permission, PERMISSION_CODE);
                        } else {
                            //only permission to write to storage is needed
                            String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        }
                    } else {
                        //already have permission
                        openCamera();
                    }
                } else {
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

                String text_heads = DB.bitmapToString(h_bitmap);
                String text_tails = DB.bitmapToString(t_bitmap);

                Boolean checkSave = DB.saveCoinData(text_info, text_type, text_quantity, text_heads, text_tails);

                if (checkSave == true) {
                    Toast.makeText(insert_coin.this, "New Coin(s) Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(insert_coin.this, "New Coin(s) Not Saved", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void gotoMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void openCamera() {
        //create camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //gets called when image is captured from camera

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && coinFace == 1) {
            Bundle extras = data.getExtras();
            h_bitmap = (Bitmap) extras.get("data");
            himageView.setImageBitmap(h_bitmap);
            //saveImage(coinFace);

        } else if (resultCode == RESULT_OK && coinFace == 2) {
            Bundle extras = data.getExtras();
            t_bitmap = (Bitmap) extras.get("data");
            timageView.setImageBitmap(t_bitmap);
            //saveImage(coinFace);
        }


    }

    public void saveImage(int coinFace) {
        //create a timestamp to serve as the image name
        String time_stamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        // create image name
        String image_name = time_stamp + ".PNG";
        File dir;
        dir = new File(path + "/COindex/");
        dir.mkdirs();
        File file = new File(dir, image_name);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);

            if (coinFace == 1) {
                h_bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else if (coinFace == 2) {
                t_bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            }

            out.flush();
            out.close();
            Toast.makeText(this, image_name + " saved to" + dir, Toast.LENGTH_SHORT).show();

            if (coinFace == 1) {
                h_file = file;
            } else if (coinFace == 2) {
                t_file = file;
            }
        } catch (Exception e) {
            //failed to save image
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
