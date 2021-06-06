package com.example.coindex;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class databaseHelper extends SQLiteOpenHelper {
    public databaseHelper(Context context) {
        super(context, "Userdata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase dataBase) {
        dataBase.execSQL("create Table CoinDetails(info TEXT Primary Key)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int i, int i1) {

    }
}
