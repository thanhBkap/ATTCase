package com.att.attcase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChonKhungLayout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_khung_layout);

        Button btnMove3 = (Button) findViewById(R.id.btn_chuyen_trang_3);
        btnMove3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChonKhungLayout.this,XayDungCase.class);
                startActivity(intent);
            }
        });
    }
}
