package com.yongling.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.yongling.coolweather.DB.DBHelper;
import com.yongling.coolweather.DB.DBManager;
import com.yongling.coolweather.HttpUtils.Httpdownload;
import com.yongling.coolweather.JsonUtils.JsonParser;
import com.yongling.coolweather.model.WeatherInfo;
import com.yongling.coolweather.model.WeatherInfo2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView city, tmpnow, conditiontxt, updatetime, today, tomorrow, dayaftertomorrow, todayconditiontxt, tomorrowconditiontxt,
            afterconditiontxt;
    private ImageView conditionView, todayconditionView, tomorrowconditionView, afterconditionView;
    private static final String QUERYPATH = "https://free-api.heweather.com/v5/weather?city=";
    private static final String APPKEY = "&key=f833d20b985843bba0521677c1e89aed";
    private String CITY;
    private Map conditionmap = new HashMap();
    private String to = "/";
    private String degree = "℃";
    private long exitTime = 0;
    private RelativeLayout parentLayout;

    private FloatingActionMenu menubutton;
    private FloatingActionButton refresh;
    private FloatingActionButton switchcity;
    private FloatingActionButton addcity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setConditionmap();
        init();
        setFloatingMenu();

        //判断是否已经选择了城市，没有则转到ChooseCity，如果是从Choosecity转过来，则利用sharePreference中的city直接查天气
        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("selected", false)) {
            CITY = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("city", null);
            new QueryWeatherTask().execute(QUERYPATH + CITY + APPKEY);
        } else {
            Intent intent = new Intent(MainActivity.this, ChooseCityActivity.class);
            startActivity(intent);
            finish();
        }

    }

    //初始化浮动选项
    public void setFloatingMenu() {
        menubutton = (FloatingActionMenu) findViewById(R.id.menubutton);
        refresh = (FloatingActionButton) findViewById(R.id.refresh);
        switchcity = (FloatingActionButton) findViewById(R.id.switchcity);
        addcity = (FloatingActionButton) findViewById(R.id.addcity);

        menubutton.setOnMenuButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menubutton.toggle(true);
            }
        });
        refresh.setOnClickListener(this);
        switchcity.setOnClickListener(this);
        addcity.setOnClickListener(this);

        menubutton.setClosedOnTouchOutside(true);
    }

    //处理浮动选项点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refresh:
                long lasttime = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getLong("sysupdatetime", 0);
                long gap = System.currentTimeMillis() - lasttime;
                if (gap < 3600000) {
                    Toast.makeText(MainActivity.this, "已是最新天气," + gap / 60000 + "分钟前更新", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "正在更新天气", Toast.LENGTH_SHORT).show();
                    new QueryWeatherTask().execute(QUERYPATH + CITY + APPKEY);
                }
                menubutton.close(true);
                break;
            case R.id.switchcity:
                Intent intent = new Intent(MainActivity.this, ChooseCityActivity.class);
                menubutton.close(true);
                startActivity(intent);
                //finish();
                break;
            case R.id.addcity:
                //Intent intent1 = new Intent(MainActivity.this,AddCityActivity.class);
                //menubutton.close(true);
                //startActivity(intent1);
                break;
        }
    }

    //查询天气异步任务
    class QueryWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            return Httpdownload.download(params[0]);
        }

        /*
        利用parserWeatherQuery把Json数据直接转为WeatherInfo对象
        保存到sharePreference
        显示到UI
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //System.out.println(s);
            WeatherInfo2 weatherInfo = JsonParser.parserWeatherQuery(s);
            saveWeatherInfo(weatherInfo);
            displayWeather();
            saveChosenCity(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("city",""));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    //建立Json中conditioncode对应的图片资源ID的Map
    public void setConditionmap() {
        String[] conditioncode = new String[]{"100", "101", "102", "103", "104"
                , "200", "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213"
                , "300", "301", "302", "303", "304", "305", "306", "307", "308", "309", "310", "311", "312", "313"
                , "400", "401", "402", "403", "404", "405", "406", "407"
                , "500", "501", "502", "503", "504", "507", "508"
                , "900", "901", "999"};

        int[] imageid = new int[]{R.drawable.cond100, R.drawable.cond101, R.drawable.cond102, R.drawable.cond103, R.drawable.cond104
                , R.drawable.cond200, R.drawable.cond201, R.drawable.cond202, R.drawable.cond203, R.drawable.cond204, R.drawable.cond205, R
                .drawable.cond206, R.drawable.cond207, R.drawable.cond208, R.drawable.cond209, R.drawable.cond210, R.drawable.cond211, R
                .drawable.cond212, R.drawable.cond213
                , R.drawable.cond300, R.drawable.cond301, R.drawable.cond302, R.drawable.cond303, R.drawable.cond304, R.drawable.cond305, R
                .drawable.cond306, R.drawable.cond307, R.drawable.cond308, R.drawable.cond309, R.drawable.cond310, R.drawable.cond311, R
                .drawable.cond312, R.drawable.cond313
                , R.drawable.cond400, R.drawable.cond401, R.drawable.cond402, R.drawable.cond403, R.drawable.cond404, R.drawable.cond405, R
                .drawable.cond406, R.drawable.cond407
                , R.drawable.cond500, R.drawable.cond501, R.drawable.cond502, R.drawable.cond503, R.drawable.cond504, R.drawable.cond507, R
                .drawable.cond508
                , R.drawable.cond900, R.drawable.cond901, R.drawable.cond999};

        for (int i = 0; i < conditioncode.length; i++) {
            conditionmap.put(conditioncode[i], imageid[i]);
        }
    }

    //从SharePreference中取出已经保存的天气数据，更新到UI
    public void displayWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        city.setText(preferences.getString("city", null));
        conditiontxt.setText(preferences.getString("conditiontxt", null));
        String tmpnowtxt = preferences.getString("tmpnow", null) + degree;
        tmpnow.setText(tmpnowtxt);

        updatetime.setText(preferences.getString("updatetime", null));

        String todaytxt = preferences.getString("tmpmin", null) + degree + to + preferences.getString("tmpmax", null) + degree;
        today.setText(todaytxt);
        String tomorrowtxt = preferences.getString("tmpmin2", null) + degree + to + preferences.getString("tmpmax2", null) + degree;
        tomorrow.setText(tomorrowtxt);
        String dayaftertomorrowtxt = preferences.getString("tmpmin3", null) + degree + to + preferences.getString("tmpmax3", null) + degree;
        dayaftertomorrow.setText(dayaftertomorrowtxt);

        todayconditiontxt.setText(preferences.getString("conditiontxt", null));
        tomorrowconditiontxt.setText(preferences.getString("conditiontxt2", null));
        afterconditiontxt.setText(preferences.getString("conditiontxt3", null));

        conditionView.setImageResource((int) conditionmap.get(preferences.getString("condition", "100")));
        todayconditionView.setImageResource((int) conditionmap.get(preferences.getString("condition", "100")));
        tomorrowconditionView.setImageResource((int) conditionmap.get(preferences.getString("condition2", "100")));
        afterconditionView.setImageResource((int) conditionmap.get(preferences.getString("condition3", "100")));

        parentLayout.setVisibility(View.VISIBLE);
    }

    //初始化UI控件
    public void init() {
        city = (TextView) this.findViewById(R.id.city);
        tmpnow = (TextView) this.findViewById(R.id.tmpnow);
        conditiontxt = (TextView) this.findViewById(R.id.conditiontxt);
        updatetime = (TextView) this.findViewById(R.id.updatetime);
        today = (TextView) this.findViewById(R.id.today);
        tomorrow = (TextView) this.findViewById(R.id.tomorrow);
        dayaftertomorrow = (TextView) this.findViewById(R.id.dayaftertomorrow);

        conditionView = (ImageView) this.findViewById(R.id.conditionimageView);
        todayconditionView = (ImageView) this.findViewById(R.id.todayconditionView);
        tomorrowconditionView = (ImageView) this.findViewById(R.id.tomorrowconditionView);
        afterconditionView = (ImageView) this.findViewById(R.id.afterconditionView);

        todayconditiontxt = (TextView) this.findViewById(R.id.todayconditiontxt);
        tomorrowconditiontxt = (TextView) this.findViewById(R.id.tomorrowconditiontxt);
        afterconditiontxt = (TextView) this.findViewById(R.id.afterconditiontxt);

        parentLayout = (RelativeLayout) this.findViewById(R.id.parentLayout);
        parentLayout.setVisibility(View.INVISIBLE);
    }

    //保存天气数据到SharePreference中
    public void saveWeatherInfo(WeatherInfo2 weatherInfo) {
        WeatherInfo2.HeWeather5Bean info = weatherInfo.getHeWeather5().get(0);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("sysupdatetime", System.currentTimeMillis());
        editor.putBoolean("selected", true);
        editor.putString("city", info.getBasic().getCity());
        editor.putString("condition", info.getNow().getCond().getCode());
        editor.putString("condition2", info.getDaily_forecast().get(1).getCond().getCode_d());
        editor.putString("condition3", info.getDaily_forecast().get(2).getCond().getCode_d());
        editor.putString("conditiontxt", info.getNow().getCond().getTxt());
        editor.putString("conditiontxt2", info.getDaily_forecast().get(1).getCond().getTxt_d());
        editor.putString("conditiontxt3", info.getDaily_forecast().get(2).getCond().getTxt_d());
        editor.putString("tmpmax", info.getDaily_forecast().get(0).getTmp().getMax());
        editor.putString("tmpmax2", info.getDaily_forecast().get(1).getTmp().getMax());
        editor.putString("tmpmax3", info.getDaily_forecast().get(2).getTmp().getMax());
        editor.putString("tmpmin", info.getDaily_forecast().get(0).getTmp().getMin());
        editor.putString("tmpmin2", info.getDaily_forecast().get(1).getTmp().getMin());
        editor.putString("tmpmin3", info.getDaily_forecast().get(2).getTmp().getMin());
        editor.putString("tmpnow", info.getNow().getTmp());
        editor.putString("updatetime", info.getBasic().getUpdate().getLoc());

        editor.apply();
    }

    public void saveChosenCity(String city){
        DBManager dbManager = new DBManager(getApplicationContext());
        dbManager.insertChosenCity(city);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
