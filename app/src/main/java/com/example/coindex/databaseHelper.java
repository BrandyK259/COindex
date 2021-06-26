package com.example.coindex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;
import java.sql.Blob;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.util.Base64;
import android.graphics.Matrix;

public class databaseHelper extends SQLiteOpenHelper {
    public databaseHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL(
                "create table CoinDetails (info TEXT primary key, type TEXT, quantity TEXT, heads TEXT, tails TEXT)"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop table if exists CoinDetails");
    }

    public Boolean saveCoinData(String info, String type, String quantity, String heads, String tails) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("info", info);
        contentValues.put("type", type);
        contentValues.put("quantity", quantity);
        contentValues.put("heads", heads);
        contentValues.put("tails", tails);

        long result = DB.insert("CoinDetails", null, contentValues);


        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean updateCoinData(String info, String type, String quantity) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("type", type);
        contentValues.put("quantity", quantity);

        Cursor cursor = DB.rawQuery("Select * from CoinDetails where info = ?", new String[]{info});
        if (cursor.getCount() > 0) {

            long result = DB.update("CoinDetails", contentValues, "info=?", new String[]{info});
            if (result == -1) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    public Boolean deleteCoinData(String info) {
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("Select * from CoinDetails where info = ?", new String[]{info});
        if (cursor.getCount() > 0) {

            long result = DB.delete("CoinDetails", "info=?", new String[]{info});
            if (result == -1) {
                return false;
            } else {
                return true;
            }

        } else {
            return false;
        }
    }

    public Cursor getData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from CoinDetails", null);
        return cursor;
    }

    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Bitmap resizeBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = 250 / width;
        float scaleHeight = 250 / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return resizedBitmap;
    }
}
