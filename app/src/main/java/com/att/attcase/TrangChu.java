package com.att.attcase;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.att.attcase.database.DatabaseHelper;
import com.att.attcase.model.CapNhatDuLieu;
import com.att.attcase.model.Layout;
import com.att.attcase.model.LayoutDienThoai;
import com.att.attcase.model.MauDienThoai;
import com.att.attcase.model.ThuongHieu;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.ExceptionHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;


public class TrangChu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button btn_chuyen_trang_2;
    boolean mTrangThaiThoat;
    DatabaseHelper databaseHelper;
    List<MauDienThoai> mListDienThoai;
    List<Layout> mListLayout;
    List<ThuongHieu> mListThuongHieu;
    List<LayoutDienThoai> mListLayoutDienThoai;
    List<Target> targets = new ArrayList<Target>();
    Handler mReset = new Handler();
    Runnable mResetTrangThaiThoat = new Runnable() {
        @Override
        public void run() {
            mTrangThaiThoat = false;
        }
    };
    ProgressDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        //thiết lập toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.mipmap.ic_mnu);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        addControls();
        adddEvents();
    }

    private void adddEvents() {
        btn_chuyen_trang_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mChuyenPage2 = new Intent(getApplicationContext(), ChonKhungLayout.class);
                startActivity(mChuyenPage2);
            }
        });
    }

    private void addControls() {

        databaseHelper = new DatabaseHelper(TrangChu.this);
        databaseHelper.checkDatabase(TrangChu.this);
        mTrangThaiThoat = false;
        mListDienThoai = new ArrayList<>();
        mListLayout = new ArrayList<>();
        mListThuongHieu = new ArrayList<>();
        mListLayoutDienThoai = new ArrayList<>();
        btn_chuyen_trang_2 = (Button) findViewById(R.id.btn_chuyen_trang_2);
        Intent nhanActivity = getIntent();
        //dialog thông báo xuất hiện nếu vừa đặt hàng xong
        if (nhanActivity.hasExtra("activity")) {
            if (nhanActivity.getStringExtra("activity").equals("dathang")) {
                thongBaoDatHangThanhCong();
            }
        }
        //cho lần đầu vào app sẽ kiểm tra dữ liệu
        if (DinhDang.sReloadedDatabase == 0) {
            if (DinhDang.isNetworkAvailable(TrangChu.this)) {
                kiemTraCapNhatDuLieu();
            } else {
                Toast.makeText(getApplicationContext(), "Không có kết nối mạng", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void kiemTraCapNhatDuLieu() {
        final CapNhatDuLieu capNhatDuLieu = new CapNhatDuLieu();
        SharedPreferences sharedPreferences = getSharedPreferences("capnhatdulieu", MODE_PRIVATE);
        final String capNhat = sharedPreferences.getString("db", "none");
        PostResponseAsyncTask kiemTraDuLieuTask = new PostResponseAsyncTask(getApplicationContext(), false, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                try {
                    JSONArray root = new JSONArray(s);
                    JSONObject object = root.getJSONObject(0);
                    capNhatDuLieu.setNoiDung(object.getString("noi_dung"));
                } catch (JSONException e) {
                    capNhatDuLieu.setNoiDung("none");
                    e.printStackTrace();
                }

                if (capNhatDuLieu.getNoiDung().equals(capNhat)) {

                } else {
                    mLoadingDialog = new ProgressDialog(TrangChu.this);
                    mLoadingDialog.setTitle("Đang tải");
                    mLoadingDialog.setMessage("Vui lòng đợi dữ liệu đang tải về ...");
                    mLoadingDialog.setIndeterminate(true);
                    mLoadingDialog.show();
                    databaseHelper.xoaDuLieu(TrangChu.this);
                    //lấy dữ liệu từ server trong trường hợp chưa load database
                    PostResponseAsyncTask postResponseAsyncTask = new PostResponseAsyncTask(TrangChu.this, false, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            try {
                                JSONObject root = new JSONObject(s);
                                JSONArray jsonArrayThuongHieu = root.getJSONArray("thuonghieu");
                                JSONArray jsonArrayLayout = root.getJSONArray("layout");
                                JSONArray jsonArrayDienThoai = root.getJSONArray("dienthoai");
                                JSONArray jsonArrayLayoutDienThoai = root.getJSONArray("layoutdienthoai");
                                //lưu điện thoại vào sqlite
                                for (int i = 0; i < jsonArrayDienThoai.length(); i++) {
                                    final int maxIndex = jsonArrayDienThoai.length() - 1;
                                    JSONObject object = jsonArrayDienThoai.getJSONObject(i);
                                    final MauDienThoai mauDienThoai = new MauDienThoai();
                                    mauDienThoai.setName(object.getString("ten"));
                                    mauDienThoai.setId(object.getString("id"));
                                    mauDienThoai.setGia(object.getString("gia"));
                                    mauDienThoai.setIdThuongHieu(object.getString("thuong_hieu_id"));
                                    mauDienThoai.setLinkAnh(DinhDang.URL_ANH + "/images/" + object.getString("anh"));
                                    mauDienThoai.setLinkAnhMatSau(DinhDang.URL_ANH + "/images/" + object.getString("anh_mat_sau"));
                                    mauDienThoai.setLinkAnhKhongChe(DinhDang.URL_ANH + "/images/" + object.getString("anh_khong_che"));
                                    final int finalI = i;
                                    Target target = new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            mauDienThoai.setAnh(bitmapToByteArray(bitmap));
                                            if (mauDienThoai.getAnhKhongChe() != null && mauDienThoai.getAnhMatSau() != null) {
                                                mListDienThoai.add(mauDienThoai);
                                                databaseHelper.insertDienThoai(mauDienThoai);
                                                if (finalI == maxIndex) {
                                                    mLoadingDialog.dismiss();
                                                    DinhDang.sReloadedDatabase = 1;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {

                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                        }
                                    };
                                    targets.add(target);
                                    Target target2 = new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            mauDienThoai.setAnhKhongChe(TrangChu.bitmapToByteArray(bitmap));
                                            if (mauDienThoai.getAnhMatSau() != null && mauDienThoai.getAnh() != null) {
                                                //   Toast.makeText(TrangChu.this, mauDienThoai.getAnh().length + "n2", Toast.LENGTH_SHORT).show();
                                                mListDienThoai.add(mauDienThoai);
                                                databaseHelper.insertDienThoai(mauDienThoai);
                                                if (finalI == maxIndex) {
                                                    mLoadingDialog.dismiss();
                                                    DinhDang.sReloadedDatabase = 1;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {

                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                        }
                                    };
                                    targets.add(target2);
                                    Target target3 = new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            mauDienThoai.setAnhMatSau(TrangChu.bitmapToByteArray(bitmap));
                                            if (mauDienThoai.getAnhKhongChe() != null && mauDienThoai.getAnh() != null) {
                                                mListDienThoai.add(mauDienThoai);
                                                databaseHelper.insertDienThoai(mauDienThoai);
                                                if (finalI == maxIndex) {
                                                    mLoadingDialog.dismiss();
                                                    DinhDang.sReloadedDatabase = 1;
                                                }
                                            }
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        }
                                    };
                                    targets.add(target3);
                                    Picasso.with(TrangChu.this).load(mauDienThoai.getLinkAnh()).into(target);
                                    Picasso.with(TrangChu.this).load(mauDienThoai.getLinkAnhKhongChe()).into(target2);
                                    Picasso.with(TrangChu.this).load(mauDienThoai.getLinkAnhMatSau()).into(target3);

                                }

                                // lưu thương hiệu vào sqlite
                                for (int i = 0; i < jsonArrayThuongHieu.length(); i++) {
                                    JSONObject object = jsonArrayThuongHieu.getJSONObject(i);
                                    final ThuongHieu thuongHieu = new ThuongHieu();
                                    thuongHieu.setName(object.getString("ten"));
                                    thuongHieu.setId(object.getString("id"));
                                    thuongHieu.setLinkAnh(DinhDang.URL_ANH + "/images/" + object.getString("logo"));
                                    Target target = new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            thuongHieu.setAnh(TrangChu.bitmapToByteArray(bitmap));
                                            mListThuongHieu.add(thuongHieu);
                                            databaseHelper.insertThuongHieu(thuongHieu);
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        }
                                    };
                                    targets.add(target);
                                    Picasso.with(TrangChu.this).load(thuongHieu.getLinkAnh()).into(target);

                                }

                                // lưu layout vào sqlite
                                for (int i = 0; i < jsonArrayLayout.length(); i++) {
                                    JSONObject object = jsonArrayLayout.getJSONObject(i);
                                    final Layout layout = new Layout();
                                    layout.setId(object.getString("id"));
                                    layout.setSoHang(object.getInt("so_dong"));
                                    layout.setSoCot(object.getInt("so_cot"));
                                    layout.setTen(object.getString("ten"));
                                    layout.setLinkAnh(DinhDang.URL_ANH + "/images/" + object.getString("anh"));
                                    Target target = new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            layout.setAnh(bitmapToByteArray(bitmap));
                                            mListLayout.add(layout);
                                            databaseHelper.insertLayout(layout);
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {
                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                                        }
                                    };
                                    targets.add(target);
                                    Picasso.with(TrangChu.this).load(layout.getLinkAnh()).into(target);
                                }

                                // lưu layout-điện thoại vào sqlite
                                for (int i = 0; i < jsonArrayLayoutDienThoai.length(); i++) {
                                    JSONObject object = jsonArrayLayoutDienThoai.getJSONObject(i);
                                    LayoutDienThoai layoutDienThoai = new LayoutDienThoai();
                                    layoutDienThoai.setId(object.getInt("id"));
                                    layoutDienThoai.setDienThoaiId(object.getInt("dien_thoai_id"));
                                    layoutDienThoai.setLayoutId(object.getInt("layout_id"));
                                    mListLayoutDienThoai.add(layoutDienThoai);
                                    databaseHelper.insertLayoutDienThoai(layoutDienThoai);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    postResponseAsyncTask.setEachExceptionsHandler(new EachExceptionsHandler() {
                        @Override
                        public void handleIOException(IOException e) {

                        }

                        @Override
                        public void handleMalformedURLException(MalformedURLException e) {

                        }

                        @Override
                        public void handleProtocolException(ProtocolException e) {

                        }

                        @Override
                        public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {

                        }
                    });
                    postResponseAsyncTask.execute(DinhDang.URL + "/phone_case_json.php");
                    //lưu biến cập nhật dữ liệu
                    SharedPreferences luuCapNhatDuLieu = getSharedPreferences("capnhatdulieu", MODE_PRIVATE);
                    SharedPreferences.Editor editor = luuCapNhatDuLieu.edit();
                    editor.putString("db", capNhatDuLieu.getNoiDung());
                    editor.commit();
                }
            }
        });
        kiemTraDuLieuTask.setExceptionHandler(new ExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                if (capNhat.equals("none")) {
                    capNhatDuLieu.setNoiDung("none");
                }
            }
        });
        kiemTraDuLieuTask.execute(DinhDang.URL + "/cap_nhat_du_lieu_json.php");
    }

    public static byte[] bitmapToByteArray(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void thongBaoDatHangThanhCong() {
        final AlertDialog thongBaoThanhCong = new AlertDialog.Builder(this).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create();
        thongBaoThanhCong.setTitle("Thành Công");
        thongBaoThanhCong.setIcon(R.drawable.ic_success);
        thongBaoThanhCong.setMessage("Chúc mừng bạn đã đặt hàng thành công !!!");
        thongBaoThanhCong.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                thongBaoThanhCong.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.lightGreen));
            }
        });
        thongBaoThanhCong.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (mTrangThaiThoat) {
            finish();
        }
        mTrangThaiThoat = true;
        Toast.makeText(TrangChu.this, "Click quay lại thêm lần nữa để thoát", Toast.LENGTH_SHORT).show();
        mReset.postDelayed(mResetTrangThaiThoat, 2000);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_cart) {
            Toast.makeText(getApplicationContext(), "Tính năng sẽ được update trong thời gian sớm nhất", Toast.LENGTH_LONG).show();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }  else if (id == R.id.nav_make_case) {
            Intent chuyenTrangChonLayout = new Intent(TrangChu.this, ChonKhungLayout.class);
            startActivity(chuyenTrangChonLayout);
        } else if (id == R.id.nav_profile) {
            Toast.makeText(getApplicationContext(), "Tính năng sẽ được update trong thời gian sớm nhất", Toast.LENGTH_LONG).show();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_store) {
            Toast.makeText(getApplicationContext(), "Tính năng sẽ được update trong thời gian sớm nhất", Toast.LENGTH_LONG).show();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_view) {
            Toast.makeText(getApplicationContext(), "Tính năng sẽ được update trong thời gian sớm nhất", Toast.LENGTH_LONG).show();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }
}
