package com.agungsubastian.kasirpintar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.agungsubastian.kasirpintar.Adapter.KodePosAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PosActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ListView kecamatan;
    private String url = "https://kodepos-2d475.firebaseio.com/kota_kab/";
    private ArrayList<HashMap<String, String>> ListData = new ArrayList<HashMap<String, String>>();
    private KodePosAdapter adapter;
    private String kode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pDialog = new ProgressDialog(PosActivity.this);
        pDialog.setIndeterminate(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setTitle("Info");
        pDialog.setMessage("Mengambil Data..");

        kecamatan = findViewById(R.id.listPos);

        Intent a = getIntent();
        kode = a.getStringExtra(MainActivity.ID);
        String judul = a.getStringExtra(MainActivity.NAMA);
        getSupportActionBar().setTitle(judul);

        ambilKecamatan();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
    private void ambilKecamatan(){
        pDialog.show();
        new AsyncHttpClient().get(url+kode+".json?print=pretty", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String rs = new String(responseBody);
                try {
                    JSONArray array= new JSONArray(rs);
                    for (int a = 0; a < array.length(); a++) {
                        JSONObject js = array.getJSONObject(a);
                        HashMap<String, String> maps = new HashMap<String, String>();
                        String kec = js.getString("kecamatan");
                        String kel = js.getString("kelurahan");
                        String pos = js.getString("kodepos");
                        maps.put(MainActivity.KEC, kec);
                        maps.put(MainActivity.LURAH, kel);
                        maps.put(MainActivity.POS, pos);
                        ListData.add(maps);
                        adapter = new KodePosAdapter(PosActivity.this, ListData);
                        kecamatan.setAdapter(adapter);
                        kecamatan.setSelection(adapter.getCount() - 1);
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
