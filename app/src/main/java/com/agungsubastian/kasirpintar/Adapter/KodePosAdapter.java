package com.agungsubastian.kasirpintar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.agungsubastian.kasirpintar.MainActivity;
import com.agungsubastian.kasirpintar.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class KodePosAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private ArrayList<HashMap<String, String>> dataSet;
    private static LayoutInflater inflater = null;

    public KodePosAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        dataSet = new ArrayList<>();
        dataSet.addAll(data);
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_kodepos, null);


        HashMap<String, String> daftar = new HashMap<String, String>();
        daftar = data.get(position);
        TextView kec = vi.findViewById(R.id.kecamatan);
        TextView lurah = vi.findViewById(R.id.kelurahan);
        TextView pos = vi.findViewById(R.id.kodepos);

        kec.setText(daftar.get(MainActivity.KEC));
        lurah.setText(daftar.get(MainActivity.LURAH));
        pos.setText(daftar.get(MainActivity.POS));

        return vi;
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        data.clear();
        if (charText.length() == 0) {
            data.addAll(dataSet);
        } else {
            for (int i=0;i<dataSet.size();i++) {
                String hasil = dataSet.get(i).get(MainActivity.KEC);
                String hasil2 = dataSet.get(i).get(MainActivity.LURAH);
                if (hasil.toLowerCase(Locale.getDefault()).contains(charText) || hasil2.toLowerCase(Locale.getDefault()).contains(charText)) {
                    data.add(dataSet.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}


