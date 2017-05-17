package com.yongling.coolweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.yongling.coolweather.DB.DBManager;

import java.util.ArrayList;

public class AddCityActivity extends Activity implements View.OnClickListener {

    private ListView chosenCityList;
    private FloatingActionMenu floatingActionMenu;
    private FloatingActionButton delButton, addButton;
    private SimpleCursorAdapter adapter;
    private ArrayList<String> selectlist = new ArrayList<>();
    private ArrayList<String> addlist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        chosenCityList = (ListView) this.findViewById(R.id.chosenCityList);
        chosenCityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(AddCityActivity.this, "list", Toast.LENGTH_SHORT).show();
                TextView textView = (TextView) view.findViewById(R.id.choose_prov);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putBoolean("selected", true);
                editor.putString("city", textView.getText().toString());
                editor.apply();
                Intent intent = new Intent(AddCityActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        setFloatingButton();
        new QueryChosenCitylistTask().execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delcity:
                final String[] delCityList = addlist.toArray(new String[addlist.size()]);
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddCityActivity.this);
                builder.setTitle("请选择要删除的城市");
                builder.setMultiChoiceItems(delCityList, null, new DialogInterface
                        .OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            selectlist.add(delCityList[which]);
                        } else {
                            selectlist.remove(delCityList[which]);
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBManager dbManager = new DBManager(getApplicationContext());
                        for (int i = 0; i < selectlist.size(); i++) {
                            dbManager.deleteChosenCity(selectlist.get(i));
                        }
                        selectlist.clear();
                        new QueryChosenCitylistTask().execute();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectlist.clear();
                    }
                });
                builder.setCancelable(false);
                builder.create().show();
                floatingActionMenu.toggle(true);
                break;
            case R.id.addmorecity:
                Intent intent = new Intent(AddCityActivity.this,ChooseCityActivity.class);
                intent.putExtra("flag","addmorecity");
                startActivity(intent);
                floatingActionMenu.toggle(true);
                finish();
                break;
        }
    }

    class QueryChosenCitylistTask extends AsyncTask<String, Void, Cursor> {

        private DBManager dbManager = new DBManager(getApplicationContext());

        @Override
        protected Cursor doInBackground(String... params) {
            return dbManager.queryChosenCity();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.chooselist, cursor, new String[]{"city"}, new int[]{R.id
                    .choose_prov}, 0);

            chosenCityList.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            cursor.moveToFirst();
            addlist.clear();
            for (int i = 0; i < cursor.getCount(); i++) {
                addlist.add(cursor.getString(cursor.getColumnIndex("city")));
                cursor.moveToNext();
            }
        }
    }

    public void setFloatingButton() {
        floatingActionMenu = (FloatingActionMenu) this.findViewById(R.id.editcityButton);
        delButton = (FloatingActionButton) this.findViewById(R.id.delcity);
        addButton = (FloatingActionButton) this.findViewById(R.id.addmorecity);

        delButton.setOnClickListener(this);
        addButton.setOnClickListener(this);

        floatingActionMenu.setClosedOnTouchOutside(true);
    }


    /*
    class MyCusorAdapter extends SimpleCursorAdapter {

        Cursor cursor;

        public MyCusorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
            this.cursor = c;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = LayoutInflater.from(AddCityActivity.this).inflate(R.layout.chooselist, null);
            } else {
                view = convertView;
            }
            cursor.moveToPosition(position);
            TextView textView = (TextView) view.findViewById(R.id.choose_prov);
            textView.setText(cursor.getString(cursor.getColumnIndex("city")));

            return view;
        }

    }
    */

}
