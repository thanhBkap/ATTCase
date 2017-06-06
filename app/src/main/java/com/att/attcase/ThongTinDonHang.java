package com.att.attcase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.widget.Toast.makeText;

public class ThongTinDonHang extends AppCompatActivity {
    public static String giatien;
    public static String tenmay;
    TextView txtTen,txtGia,txtSoLuong;
    Button btnBack,btnXacNhan;
    ImageView imageCaseDh;
    private long mDatHangClick;
    ArrayList<String> dsAnhString;
    private Intent getIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_don_hang);
        addControls();
    }

    private void addControls() {
        getIntent = getIntent();
        dsAnhString = (ArrayList<String>) getIntent.getExtras().getSerializable("DsAnh");
        txtTen = (TextView) findViewById(R.id.text1_dh);
        txtGia = (TextView) findViewById(R.id.text2_dh);
        txtSoLuong = (TextView) findViewById(R.id.text3_dh);

        txtTen.setText("    Tên Máy : " + tenmay );
        txtGia.setText("    Giá Tiền : " + giatien);
        txtSoLuong.setText("    Số Lượng : 1");

        btnBack = (Button) findViewById(R.id.btn_quay_lai_dh);
        btnXacNhan = (Button) findViewById(R.id.btn_xacnhan_dh);

        Uri uriImg = Uri.parse(dsAnhString.get(0));
        imageCaseDh = (ImageView) findViewById(R.id.img_case_dh);
        imageCaseDh.setImageURI(uriImg);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long mDatHangXacNhan = 3500;
                long currentTime = System.currentTimeMillis();
                if (Math.abs(currentTime - mDatHangClick) > mDatHangXacNhan) {
                    makeText(ThongTinDonHang.this, "mời bạn nhấn thêm lần nữa để xác nhận đơn đặt hàng", Toast.LENGTH_SHORT).show();
                    mDatHangClick = currentTime;
                } else {
                    Intent intentDatHang = new Intent(ThongTinDonHang.this, DatHang.class);
                    intentDatHang.putExtra("DsAnh", dsAnhString);
                    startActivity(intentDatHang);
                }
            }
        });
    }
}
