package com.agungsubastian.kasirpintar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.agungsubastian.kasirpintar.Adapter.ItemAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView provinsi;
    private String url = "https://kodepos-2d475.firebaseio.com/list_propinsi.json?print=pretty";
    private ArrayList<HashMap<String, String>> ListData = new ArrayList<HashMap<String, String>>();
    private ItemAdapter adapter;
    public static String ID = "id", NAMA = "nama", KEC = "kec", LURAH = "lurah", POS = "pos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("List Provinsi");

        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setIndeterminate(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setTitle("Info");
        pDialog.setMessage("Mengambil Data..");

        provinsi = findViewById(R.id.listProvinsi);

        ambilProvinsi();

        provinsi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String,String> map = ListData.get(i);
                Intent a = new Intent(getApplicationContext(), KabupatenActivity.class);
                a.putExtra(ID, map.get(ID));
                a.putExtra(NAMA, map.get(NAMA));
                startActivity(a);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.cari) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    if(adapter != null){
                        adapter.filter(s);
                    }
                    return false;
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void ambilProvinsi(){
        pDialog.show();
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String rs = new String(responseBody);
                try {
                    JSONObject jsonObj = new JSONObject(rs);
                    Iterator<String> keys = jsonObj.keys();
                    while(keys.hasNext()){
                        HashMap<String, String> maps = new HashMap<String, String>();
                        String key = keys.next();
                        String value = jsonObj.getString(key);
                        maps.put(MainActivity.ID, key);
                        maps.put(MainActivity.NAMA, value);
                        ListData.add(maps);
                        adapter = new ItemAdapter(MainActivity.this, ListData);
                        provinsi.setAdapter(adapter);
                        provinsi.setSelection(adapter.getCount() - 1);
                        adapter.notifyDataSetChanged();
                    }
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
