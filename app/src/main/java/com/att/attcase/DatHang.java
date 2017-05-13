package com.att.attcase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.photoutil.ImageBase64;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatHang extends AppCompatActivity {
    RelativeLayout llDatHang;
    private Toolbar mToolbar;
    private Button btnQuayLai, btnDatHang;
    private EditText txtTen, txtDiaChi, txtSoDienThoai, txtEmail;
    List<Bitmap> listAnh;
    int soReq=0;
    int loi = 0;
    ProgressDialog mLoadingDialog;
    Intent getIntent;
    ArrayList<String> arrayListAnhDuocChon;
    Uri[]       uris;
    List<Bitmap>    bmAnhDuocChon; // bitmap anh truyen tu trang 3
    int i = 0;
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
    private void getIntentDatHang() {
        getIntent = getIntent();
        arrayListAnhDuocChon = (ArrayList<String>) getIntent.getExtras().getSerializable("DsAnh");
                uris = new Uri[arrayListAnhDuocChon.size()];
                for (String e : arrayListAnhDuocChon) {
                       uris[i] = Uri.parse(e);
                       i++;
                }

        for (int i = 0; i < uris.length;i++) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uris[i]);
                bmAnhDuocChon.add(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void addControls(){
        //thiết lập action bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //đói chiếu
        btnQuayLai = (Button) mToolbar.findViewById(R.id.btn_quay_lai);
        btnDatHang = (Button) findViewById(R.id.btn_dat_hang);
        txtDiaChi = (EditText) findViewById(R.id.txt_dia_chi);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtSoDienThoai = (EditText) findViewById(R.id.txt_so_dien_thoai);
        txtTen = (EditText) findViewById(R.id.txt_ten);
        llDatHang = (RelativeLayout) findViewById(R.id.ll_dathang);
        //khởi tạo và thêm dữ liệu
        listAnh = new ArrayList<>();
        listAnh.add(drawableToBitmap(getResources().getDrawable(R.drawable.a1)));
        listAnh.add(drawableToBitmap(getResources().getDrawable(R.drawable.a2)));
        listAnh.add(drawableToBitmap(getResources().getDrawable(R.drawable.a3)));
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        /*int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);*/

        return ((BitmapDrawable) drawable).getBitmap();


    }

    private void xuLyDatHang() {
        if (kiemTraTenHopLe() && kiemTraDiaChiHopLe() && kiemTraSoDienThoaiHopLe() && kiemTraEmailHopLe()) {
            mLoadingDialog = new ProgressDialog(DatHang.this);
            mLoadingDialog.setTitle("Đang tải");
            mLoadingDialog.setMessage("Vui lòng đợi đặt hàng ...");
            mLoadingDialog.setIndeterminate(true);
            mLoadingDialog.show();
            final MyCommand myCommand = new MyCommand(getApplicationContext());
            for (int i = 0; i < listAnh.size(); i++) {
                try {
                    Bitmap bitmap = listAnh.get(i);
                    final String encodedBitMap = ImageBase64.encode(bitmap);
                    String url = DinhDang.URL + "/dathang.php";
                    StringRequest stringRequest;
                    if (i == 0) {
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
                                map.put("ten", txtTen.getText().toString());
                                map.put("diachi", txtDiaChi.getText().toString());
                                map.put("sodienthoai", txtSoDienThoai.getText().toString());
                                map.put("email", txtEmail.getText().toString());
                                map.put("image", encodedBitMap);
                                return map;
                            }
                        };
                        myCommand.add(stringRequest);
                        soReq++;

                    } else {

                        final int finalI = i;
                        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (finalI == (listAnh.size() - 1)) {
                                    mLoadingDialog.dismiss();
                                    Intent quayVeTrangChu = new Intent(DatHang.this, TrangChu.class);
                                    quayVeTrangChu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    quayVeTrangChu.putExtra("activity", "dathang");
                                    startActivity(quayVeTrangChu);
                                    finish();

                                }
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
                                map.put("loop", "1");
                                map.put("image", encodedBitMap);
                                return map;
                            }
                        };
                        myCommand.add(stringRequest);
                        soReq++;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            myCommand.execute();
            //Intent quayVeTrangChu = new Intent(DatHang.this, Test.class);
            //quayVeTrangChu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //quayVeTrangChu.putExtra("activity", "dathang");
            //startActivity(quayVeTrangChu);
            //  finish();
           // Toast.makeText(getApplicationContext(),"Số req: "+soReq,Toast.LENGTH_SHORT).show();
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
