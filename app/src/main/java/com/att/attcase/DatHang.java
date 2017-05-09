package com.att.attcase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

public class DatHang extends AppCompatActivity {
    RelativeLayout llDatHang;
    private Toolbar mToolbar;
    private Button btnQuayLai, btnDatHang;
    private EditText txtTen, txtDiaChi, txtSoDienThoai, txtEmail;
    ImageView[] imgDatHang;
    int slAnh;
    Bitmap[] mBitMapDatHang;
    List<Bitmap> listAnh;
    int loi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dat_hang);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyDatHang();
            }
        });
    }

    private void addControls() {
        //thiết lập action bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        btnQuayLai = (Button) mToolbar.findViewById(R.id.btn_quay_lai);
        btnDatHang = (Button) findViewById(R.id.btn_dat_hang);
        txtDiaChi = (EditText) findViewById(R.id.txt_dia_chi);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtSoDienThoai = (EditText) findViewById(R.id.txt_so_dien_thoai);
        txtTen = (EditText) findViewById(R.id.txt_ten);
        llDatHang = (RelativeLayout) findViewById(R.id.ll_dathang);

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

    private void xuLyDatHang() {
        if (kiemTraTenHopLe() && kiemTraDiaChiHopLe() && kiemTraSoDienThoaiHopLe() && kiemTraEmailHopLe()) {
           /* final MyCommand myCommand = new MyCommand(getApplicationContext());
            for (int i = 0; i < listAnh.size(); i++) {
                try {
                    Bitmap bitmap = listAnh.get(i);
                    final String encodedBitMap = ImageBase64.encode(bitmap);
                    String url = "/taogianhangjson2.php";
                    StringRequest stringRequest;
                    stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            loi++;
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("loop", "0");
                            map.put("image", encodedBitMap);
                            return map;
                        }
                    };
                    myCommand.add(stringRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            myCommand.execute();*/
            Intent quayVeTrangChu = new Intent(DatHang.this, TrangChu.class);
            quayVeTrangChu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            quayVeTrangChu.putExtra("activity", "dathang");
            startActivity(quayVeTrangChu);

        }
    }

    private boolean kiemTraEmailHopLe() {
        if (!Check.isEmailValid(txtEmail.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean kiemTraSoDienThoaiHopLe() {
        if (txtSoDienThoai.getText().toString().length() < 8) {
            Toast.makeText(getApplicationContext(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean kiemTraTenHopLe() {
        if (txtTen.getText().toString().length() < 2) {
            Toast.makeText(getApplicationContext(), "Họ tên không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean kiemTraDiaChiHopLe() {
        if (txtDiaChi.getText().toString().length() < 5) {
            Toast.makeText(getApplicationContext(), "Địa chỉ không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
