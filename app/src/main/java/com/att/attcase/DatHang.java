package com.att.attcase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.util.ArrayList;

public class DatHang extends AppCompatActivity {
    Intent getIntent;
    ArrayList<String> arrayListAnhDuocChon;
    Uri[]       uris;
    ImageView imgtest;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_hang);
        getIntentDatHang();
    }

    private void getIntentDatHang() {
        getIntent = getIntent();
        arrayListAnhDuocChon = (ArrayList<String>) getIntent.getExtras().getSerializable("DsAnh");
        uris = new Uri[arrayListAnhDuocChon.size()];
        for (String e : arrayListAnhDuocChon) {
            uris[i] = Uri.parse(e);
            i++;
        }
    }
}
