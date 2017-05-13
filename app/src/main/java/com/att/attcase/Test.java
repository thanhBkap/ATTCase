package com.att.attcase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent quayVeTrangChu = new Intent(Test.this, TrangChu.class);
        quayVeTrangChu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        quayVeTrangChu.putExtra("activity", "dathang");
        startActivity(quayVeTrangChu);
        finish();
    }
}
