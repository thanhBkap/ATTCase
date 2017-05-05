package com.att.attcase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.att.attcase.kho_anh.AnhDuocChon;

import java.util.ArrayList;

public class DatHang extends AppCompatActivity {
    LinearLayout llDatHang;
    ImageView[]  imgDatHang;
    int i = 0;
    ArrayList<AnhDuocChon> danhsachanh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_hang);

        getIntentDatHang();
        addControls();
    }

    private void getIntentDatHang() {
        danhsachanh = (ArrayList<AnhDuocChon>) getIntent().getSerializableExtra("danhsachanh");
    }

    private void addControls() {
        llDatHang = (LinearLayout) findViewById(R.id.ll_dathang);
//        for (AnhDuocChon s : danhsachanh){
//            imgDatHang[i] = new ImageView(this);
//            imgDatHang[i].setImageBitmap(danhsachanh.get(i).getBmHinhAnh());
//            llDatHang.addView(imgDatHang[i]);
//            i++;
//        }
    }


}
