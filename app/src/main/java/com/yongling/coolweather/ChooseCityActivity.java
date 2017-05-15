package com.yongling.coolweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yongling.coolweather.DB.DBManager;
import com.yongling.coolweather.HttpUtils.Httpdownload;
import com.yongling.coolweather.JsonUtils.JsonParser;


public class ChooseCityActivity extends Activity {

    private ListView listView;
    private SimpleCursorAdapter adapter;
    private static final int PROV_LEVEL = 1;
    private static final int CITY_LEVEL = 2;
    private static final int QUERY_LEVEL = 3;
    private int CURRENT_LEVEL = 1;
    private static final String CITYLISTPATH = "http://files.heweather.com/china-city-list.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_city);
        listView = (ListView) this.findViewById(R.id.citylist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.choose_prov);
                if (CURRENT_LEVEL == QUERY_LEVEL) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                    editor.putBoolean("selected", true);
                    editor.putString("city", textView.getText().toString());
                    editor.apply();
                    CURRENT_LEVEL = 1;
                    Intent intent = new Intent(ChooseCityActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    new QueryCitylistTask().execute(textView.getText().toString());
                }
            }
        });
        loadCityList();

    }

    //加载城市列表
    public void loadCityList() {
        if (getDatabasePath("citylist_heweather.db").exists()) {
            //Toast.makeText(this, "本地数据库已存在，正在查询...", Toast.LENGTH_LONG).show();
            new QueryCitylistTask().execute();
        } else {
            Toast.makeText(this, "正在初始化...", Toast.LENGTH_LONG).show();
            new DownloadCityListTask().execute(CITYLISTPATH);
        }
    }


    //根据CURRENT_LEVEL标志位向数据库查询相应的省份或城市
    class QueryCitylistTask extends AsyncTask<String, Void, Cursor> {

        private DBManager dbManager = new DBManager(getApplicationContext());

        @Override
        protected Cursor doInBackground(String... params) {
            if (CURRENT_LEVEL == PROV_LEVEL) {
                CURRENT_LEVEL = CITY_LEVEL;
                return dbManager.queryProv();
            } else {
                CURRENT_LEVEL = QUERY_LEVEL;
                return dbManager.queryCity(params[0]);
            }
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if (CURRENT_LEVEL == CITY_LEVEL) {
                adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.chooselist, cursor, new String[]{"prov"}, new int[]{R.id
                        .choose_prov}, 0);
            } else {
                adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.chooselist, cursor, new String[]{"city"}, new int[]{R.id
                        .choose_prov}, 0);
            }

            //dbManager.closeDatabase();

            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }


    //如果数据库不存在，则向服务器查询，再显示城市列表
    class DownloadCityListTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return Httpdownload.download(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //利用parserCitylistToDB方法把数据存入数据库
            JsonParser.parserCitylistToDB(s, getApplicationContext());
            new QueryCitylistTask().execute();
        }
    }

    //处理后退逻辑
    @Override
    public void onBackPressed() {
        if (CURRENT_LEVEL == QUERY_LEVEL) {
            CURRENT_LEVEL = PROV_LEVEL;
            new QueryCitylistTask().execute();
        } else if (CURRENT_LEVEL == CITY_LEVEL && !PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean
                ("selected", false)) {
            Toast.makeText(this, "请选择想要查询的城市！", Toast.LENGTH_LONG).show();
        } else {
            super.onBackPressed();
        }
    }
}
