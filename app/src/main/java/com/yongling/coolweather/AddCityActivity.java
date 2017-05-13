package com.yongling.coolweather;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.yongling.coolweather.DB.DBManager;

public class AddCityActivity extends AppCompatActivity {

    private ListView chosenCityList;
    private FloatingActionMenu floatingActionMenu;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        chosenCityList = (ListView) this.findViewById(R.id.chosenCityList);
        floatingActionMenu = (FloatingActionMenu) this.findViewById(R.id.addcityButton);
        new QueryChosenCitylistTask().execute();

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
        }
    }
}
