package com.example.coindex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class databaseHelper extends SQLiteOpenHelper {
    public databaseHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table CoinDetails(info TEXT primary key, type TEXT, quantity TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop table if exists CoinDetails");
    }

    public Boolean save_coin_data(String info, String type, String quantity){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("info", info);
        contentValues.put("type", type);
        contentValues.put("quantity", quantity);

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
}
