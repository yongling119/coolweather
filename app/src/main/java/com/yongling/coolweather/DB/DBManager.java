package com.yongling.coolweather.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by yongling on 2016/7/20.
 */
public class DBManager {

    private DBHelper helper;
    private SQLiteDatabase database;

    public DBManager(Context context) {
        this.helper = new DBHelper(context);
        this.database = helper.getWritableDatabase();
    }

    //查所有省份
    public Cursor queryProv() {
        return database.query("citylist", new String[]{"_id", "prov"}, null, null, "prov", null, "_id", null);
    }

    //根据省份查下面的城市
    public Cursor queryCity(String prov) {
        return database.query("citylist", new String[]{"_id", "city"}, "prov=?", new String[]{prov}, null, null, "_id", null);
    }

    public Cursor queryChosenCity(){
        return database.query("chosenlist",new String[]{"_id","city"},null,null,"city",null,"_id",null);
    }

    public void insertChosenCity(String city){
        ContentValues contentValues = new ContentValues();
        contentValues.put("city",city);
        database.insert("chosenlist",null,contentValues);
        closeDatabase();
    }

    public void deleteChosenCity(String city){
        database.delete("chosenlist","city=?",new String[]{city});
    }

    public void closeDatabase() {
        if (database != null) {
            database.close();
        }
    }
}
