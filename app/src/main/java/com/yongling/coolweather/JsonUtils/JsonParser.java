package com.yongling.coolweather.JsonUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.yongling.coolweather.DB.DBHelper;
import com.yongling.coolweather.model.CityInfo;
import com.yongling.coolweather.model.WeatherInfo;
import com.yongling.coolweather.model.WeatherInfo2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by yongling on 2016/7/19.
 *
 */
public class JsonParser {

    //将Json数据转化为WeatherInfo对象
    public static WeatherInfo2 parserWeatherQuery(String data) {

        Gson gson = new Gson();
        return gson.fromJson(data,WeatherInfo2.class);

        /*try {
            JSONObject jsonObject1 = new JSONObject(data);
            JSONArray jsonObject2 = jsonObject1.getJSONArray("HeWeather data service 3.0");
            JSONObject weatherdata = jsonObject2.getJSONObject(0);

            JSONObject basic = weatherdata.getJSONObject("basic");
            weatherInfo.setCity(basic.getString("city"));
            weatherInfo.setUpdatetime(basic.getJSONObject("update").getString("loc"));

            JSONObject now = weatherdata.getJSONObject("now");
            weatherInfo.setTmpnow(now.getString("tmp"));
            weatherInfo.setConditiontxt(now.getJSONObject("cond").getString("txt"));
            weatherInfo.setCondition(now.getJSONObject("cond").getString("code"));

            JSONObject forcast = weatherdata.getJSONArray("daily_forecast").getJSONObject(0).getJSONObject("tmp");
            weatherInfo.setTmpmax(forcast.getString("max"));
            weatherInfo.setTmpmin(forcast.getString("min"));

            JSONObject forcast2 = weatherdata.getJSONArray("daily_forecast").getJSONObject(1).getJSONObject("tmp");
            weatherInfo.setTmpmax2(forcast2.getString("max"));
            weatherInfo.setTmpmin2(forcast2.getString("min"));
            weatherInfo.setCondition2(weatherdata.getJSONArray("daily_forecast").getJSONObject(1).getJSONObject("cond").getString("code_d"));
            weatherInfo.setConditiontxt2(weatherdata.getJSONArray("daily_forecast").getJSONObject(1).getJSONObject("cond").getString("txt_d"));

            JSONObject forcast3 = weatherdata.getJSONArray("daily_forecast").getJSONObject(2).getJSONObject("tmp");
            weatherInfo.setTmpmax3(forcast3.getString("max"));
            weatherInfo.setTmpmin3(forcast3.getString("min"));
            weatherInfo.setCondition3(weatherdata.getJSONArray("daily_forecast").getJSONObject(2).getJSONObject("cond").getString("code_d"));
            weatherInfo.setConditiontxt3(weatherdata.getJSONArray("daily_forecast").getJSONObject(2).getJSONObject("cond").getString("txt_d"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherInfo;*/
    }

    //将Json数据存入数据库
    public static void parserCitylistToDB(String data, Context context) {

        DBHelper helper = new DBHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();

        try {
            //JSONObject jsonObject = new JSONObject(data);
            //JSONArray city_info = jsonObject.getJSONArray("city_info");
            JSONArray city_info = new JSONArray(data);
            Gson gson = new Gson();
            database.beginTransaction();

            for (int i = 0; i < city_info.length(); i++) {
                //Gson gson = new Gson();
                CityInfo info = gson.fromJson(city_info.getJSONObject(i).toString(),CityInfo.class);
                ContentValues contentValues = new ContentValues();
                contentValues.put("id",info.getId());
                contentValues.put("city",info.getCityZh());
                contentValues.put("prov",info.getProvinceZh());
                database.insert("citylist",null,contentValues);
            }
            database.setTransactionSuccessful();
            database.endTransaction();
            database.close();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
