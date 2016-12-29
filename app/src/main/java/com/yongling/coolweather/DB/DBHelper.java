package com.yongling.coolweather.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yongling on 2016/7/20.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "citylist_heweather.db";
    private static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table citylist(_id integer primary key autoincrement,id varchar(20),city varchar(20),prov varchar(20))");
        db.execSQL("create table chosenlist(_id integer primary key autoincrement,id varchar(20),city varchar(20),prov varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
