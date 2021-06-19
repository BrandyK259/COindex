package com.example.coindex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class databaseHelper extends SQLiteOpenHelper {
    public databaseHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(
                "create table CoinDetails (info TEXT primary key, type TEXT, quantity TEXT, heads BLOB, tails BLOB)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop table if exists CoinDetails");
    }

    public Boolean save_coin_data(String info, String type, String quantity, byte[] heads, byte[] tails){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("info", info);
        contentValues.put("type", type);
        contentValues.put("quantity", quantity);
        contentValues.put("heads", heads);
        contentValues.put("tails", tails);

        long result = DB.insert("CoinDetails", null, contentValues);
        if(result == -1) {
            return false;
        } else{
            return true;
        }
    }

    public Boolean update_coin_data(String info, String type, String quantity){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("type", type);
        contentValues.put("quantity", quantity);

        Cursor cursor = DB.rawQuery("Select * from CoinDetails where info = ?", new String[]{info});
        if(cursor.getCount() >0 ) {

            long result = DB.update("CoinDetails", contentValues, "info=?", new String[]{info});
            if (result == -1) {
                return false;
            } else {
                return true;
            }

        }
        else{
            return false;
        }
    }

    public Boolean delete_coin_data(String info){
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("Select * from CoinDetails where info = ?", new String[]{info});
        if(cursor.getCount() >0 ) {

            long result = DB.delete("CoinDetails","info=?", new String[]{info});
            if (result == -1) {
                return false;
            } else {
                return true;
            }

        }
        else{
            return false;
        }
    }

    public Cursor get_data(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from CoinDetails", null);
        return cursor;
    }

    public byte[] file_to_bytes(File file){
        int size = (int)file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    //supposedly this is a function that can save the thumbnail of the camera picture, the thumbnail might be better to save
    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }*/
}
