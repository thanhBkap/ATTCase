package com.att.attcase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class DatHang extends AppCompatActivity {
    ImageView[] imgDatHang;
    int slAnh;
    Bitmap[] mBitMapDatHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_hang);
        getIntentDatHang();
    }

    private void getIntentDatHang() {
        try {
            slAnh = getIntent().getIntExtra("so_luong_anh", 1);
            mBitMapDatHang = new Bitmap[slAnh];
            imgDatHang = new ImageView[slAnh];
            for (int i = 0; i < slAnh; i++) {
                byte[] byteArray = getIntent().getByteArrayExtra("anh" + i);
                mBitMapDatHang[i] = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
