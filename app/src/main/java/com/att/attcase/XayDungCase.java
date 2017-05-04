package com.att.attcase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class XayDungCase extends AppCompatActivity {
    String idMauDienThoai,idLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xay_dung_case);
        addControls();
        addEvents();
    }

    private void addControls() {
        Intent nhanLayout = getIntent();
        idLayout=nhanLayout.getStringExtra("idLayout");
        idMauDienThoai=nhanLayout.getStringExtra("idMauDienThoai");
    }

    private void addEvents() {

    }
}
