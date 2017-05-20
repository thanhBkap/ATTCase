package com.att.attcase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.photoutil.ImageBase64;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatHang extends AppCompatActivity {
    public static String sLayoutID;
    public static String sDienThoaiID;
    private RelativeLayout llDatHang;
    private Toolbar mToolbar;
    private Button btnQuayLai, btnDatHang;
    private EditText txtTen, txtDiaChi, txtSoDienThoai, txtEmail;
    private List<Bitmap> listAnh;
    private int soReq = 0;
    private ProgressDialog mLoadingDialog;
    private Intent getIntent;
    private ArrayList<String> arrayListAnhDuocChon;
    private Uri[] uris;
    private Bitmap bitmap;
    private boolean loiRequest = false;
    private int i = 0;

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
                if (DinhDang.isNetworkAvailable(DatHang.this)){
                    xuLyDatHang();
                }
            }
        });
    }

    private List<Bitmap> getIntentDatHang() {
        List<Bitmap> bmAnhDuocChon;
        bmAnhDuocChon = new ArrayList<>();
        getIntent = getIntent();
        arrayListAnhDuocChon = (ArrayList<String>) getIntent.getExtras().getSerializable("DsAnh");
        uris = new Uri[arrayListAnhDuocChon.size()];
        for (String e : arrayListAnhDuocChon) {
            uris[i] = Uri.parse(e);
            i++;
        }

        for (int i = 0; i < uris.length; i++) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uris[i]);
                bmAnhDuocChon.add(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bmAnhDuocChon;
    }

    private void addControls() {
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
        listAnh.addAll(getIntentDatHang());
    }

    private void xuLyDatHang() {
        if (kiemTraTenHopLe() && kiemTraDiaChiHopLe() && kiemTraSoDienThoaiHopLe() && kiemTraEmailHopLe()) {
            //một dialog thể hiện việc đang up dữ liệu lên server
            mLoadingDialog = new ProgressDialog(DatHang.this);
            mLoadingDialog.setTitle("Đang tải");
            mLoadingDialog.setMessage("Vui lòng đợi đặt hàng ...");
            mLoadingDialog.setIndeterminate(true);
            mLoadingDialog.show();
            String url = DinhDang.URL + "/dathang.php";
            final MyCommand myCommand = new MyCommand(getApplicationContext());
            for (int i = 0; i < listAnh.size(); i++) {
                if (loiRequest == true) {
                    break;
                }
                try {
                    bitmap = listAnh.get(i);
                    final String encodedBitMap = ImageBase64.encode(bitmap);
                    StringRequest stringRequest;
                    //request up lên đầy đủ các thông tin trừ ảnh ghép
                    if (i == 0) {
                        final int finalI = i;
                        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //chuyển về trang chủ khi up xong
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
                                loiRequest = true;
                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                    Toast.makeText(DatHang.this,
                                            "lỗi timeout haowcj no connection",
                                            Toast.LENGTH_LONG).show();
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                    Toast.makeText(DatHang.this,
                                            "lỗi auth failure",
                                            Toast.LENGTH_LONG).show();
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    Toast.makeText(DatHang.this,
                                            "lỗi server",
                                            Toast.LENGTH_LONG).show();
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    Toast.makeText(DatHang.this,
                                            "lỗi network",
                                            Toast.LENGTH_LONG).show();
                                } else if (error instanceof ParseError) {
                                    //TODO
                                    Toast.makeText(DatHang.this,
                                            "lỗi parse error",
                                            Toast.LENGTH_LONG).show();
                                }
                                mLoadingDialog.dismiss();
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
                                map.put("dienthoaiid", sDienThoaiID);
                                map.put("layoutid", sLayoutID);
                                map.put("image", encodedBitMap);
                                return map;
                            }
                        };
                        myCommand.add(stringRequest);
                        soReq++;

                    } else {
                        //request up ảnh nhỏ
                        final int finalI = i;
                        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //chuyển về trang chủ khi up xong
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
                                loiRequest = true;
                                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                    Toast.makeText(DatHang.this,
                                            "lỗi timeout haowcj no connection",
                                            Toast.LENGTH_LONG).show();
                                } else if (error instanceof AuthFailureError) {
                                    //TODO
                                    Toast.makeText(DatHang.this,
                                            "lỗi auth failure",
                                            Toast.LENGTH_LONG).show();
                                } else if (error instanceof ServerError) {
                                    //TODO
                                    Toast.makeText(DatHang.this,
                                            "lỗi server",
                                            Toast.LENGTH_LONG).show();
                                } else if (error instanceof NetworkError) {
                                    //TODO
                                    Toast.makeText(DatHang.this,
                                            "lỗi network",
                                            Toast.LENGTH_LONG).show();
                                } else if (error instanceof ParseError) {
                                    //TODO
                                    Toast.makeText(DatHang.this,
                                            "lỗi parse error",
                                            Toast.LENGTH_LONG).show();
                                }
                                mLoadingDialog.dismiss();
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
                        //thêm vào list request
                        myCommand.add(stringRequest);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //thực thi list request
            myCommand.execute();
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
