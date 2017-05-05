package com.att.attcase;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.att.attcase.kho_anh.AnhDuocChon;
import com.att.attcase.kho_anh.KhoAnhAdapter;
import com.att.attcase.xaydungcase.KieuKhungHinh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class XayDungCase extends AppCompatActivity implements android.view.View.OnClickListener{

    //Khoi tao
    Button              btnBack,btnAnh,btnSave,btnTheme,btnTool,btnEffects,
                        btnMoKhoAnh,btnRefresh,btnRandom;
    Button[]            btnCacHieuUng;
    RelativeLayout      rlHieuUng,rlDanhSachAnh,rlXayDungCase;
    LinearLayout        llXayDungCase,llCongCu;
    static ImageView    img_Case;
    Uri                 imgUri;
    Bitmap              bitmap;
    KieuKhungHinh kieuKhungHinh;
    int                 chieuDai,chieuRong,chieuDaiCase,chieuRongCase;
    static int          toaDoX,toaDoY;
    Boolean             click;
    static ImageView[][]       dsAnhXayDungCase;

    public  static ArrayList<AnhDuocChon> arrayList;
    private static      RecyclerView        rcAnhDuocChon;
    public  static       View.OnClickListener recyclerViewClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xay_dung_case);
        addControls();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back  :
                Intent intent = new Intent(XayDungCase.this,ChonKhungLayout.class);
                startActivity(intent);
                break;

            case R.id.btn_save  :
                Intent intentDatHang = new Intent(XayDungCase.this,DatHang.class);
                intentDatHang.putExtra("danhsachanh",arrayList);
                startActivity(intentDatHang);

                break;

            case R.id.btn_anh   :
                rlHieuUng.setVisibility(View.GONE);
                rlDanhSachAnh.setVisibility(View.VISIBLE);
                llCongCu.setVisibility(View.GONE);
                break;

            case R.id.btn_congcu:
                rlHieuUng.setVisibility(View.GONE);
                rlDanhSachAnh.setVisibility(View.GONE);
                llCongCu.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_theme :
                rlHieuUng.setVisibility(View.GONE);
                rlDanhSachAnh.setVisibility(View.GONE);
                llCongCu.setVisibility(View.GONE);
                break;

            case R.id.btn_hieuung:
                rlHieuUng.setVisibility(View.VISIBLE);
                rlDanhSachAnh.setVisibility(View.GONE);
                llCongCu.setVisibility(View.GONE);
                break;

            case R.id.btn_hieuung1:
                break;

            case R.id.btn_mokhoanh:
                Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pick, 100);
                break;

            case R.id.btn_refresh:
                finish();
                startActivity(getIntent());
                break;

            case R.id.btn_random:
                if (arrayList.size() > 1) {
                    for (int i = 0; i < kieuKhungHinh.getSoCot();i++){
                        for (int j = 0 ; j < kieuKhungHinh.getSoHang();j++){
                            int rand = rand(0,arrayList.size() - 1);
                            dsAnhXayDungCase[i][j].setImageBitmap(arrayList.get(rand).getBmHinhAnh());
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(),"Bạn cần nhiều ảnh hơn để thực hiện chức năng này",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void addControls() {
        // list Button
        btnCacHieuUng = new Button[1];
        // Button
        btnBack     =   (Button)        findViewById(R.id.btn_back);
        btnAnh      =   (Button)        findViewById(R.id.btn_anh);
        btnSave     =   (Button)        findViewById(R.id.btn_save);
        btnTheme    =   (Button)        findViewById(R.id.btn_theme);
        btnTool     =   (Button)        findViewById(R.id.btn_congcu);
        btnEffects  =   (Button)        findViewById(R.id.btn_hieuung);
        btnMoKhoAnh =   (Button)        findViewById(R.id.btn_mokhoanh);
        btnRandom   =   (Button)        findViewById(R.id.btn_random);
        btnRefresh  =   (Button)        findViewById(R.id.btn_refresh);

        btnCacHieuUng[0] = (Button)     findViewById(R.id.btn_hieuung1);

        // Button click
        btnBack.setOnClickListener(this);
        btnAnh.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnTheme.setOnClickListener(this);
        btnTool.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnEffects.setOnClickListener(this);
        btnMoKhoAnh.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnRandom.setOnClickListener(this);
        recyclerViewClick = new recyclerViewClick(this);

        btnCacHieuUng[0].setOnClickListener(this);

        // Layout
        rlHieuUng       =   (RelativeLayout)    findViewById(R.id.rl_thietke_hieuung);
        rlDanhSachAnh   =   (RelativeLayout)    findViewById(R.id.rl_danhsachanh);
        rcAnhDuocChon   =   (RecyclerView)      findViewById(R.id.rc_anhduocchon);
        llCongCu        =   (LinearLayout)      findViewById(R.id.ll_congcu);

        //khoi tao layout
        rcAnhDuocChon.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rcAnhDuocChon.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,layoutManager.getOrientation());
        rcAnhDuocChon.addItemDecoration(dividerItemDecoration);
        arrayList = new ArrayList<AnhDuocChon>();
        KhoAnhAdapter khoAnhAdapter = new KhoAnhAdapter(arrayList,getApplicationContext());
        rcAnhDuocChon.setAdapter(khoAnhAdapter);

        //Image
        img_Case    =   (ImageView)         findViewById(R.id.img_case);

        //Thiet ke case
        kieuKhungHinh = new KieuKhungHinh(1,1);
        rlXayDungCase = (RelativeLayout) findViewById(R.id.rl_xaydungcase);
        llXayDungCase = (LinearLayout)   findViewById(R.id.ll_xaydungcase);
        //get pixel screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        chieuDai  = displayMetrics.heightPixels;
        chieuRong = displayMetrics.widthPixels;
        chieuDaiCase = (chieuDai*3)/5;
        chieuRongCase = chieuRong/2;
        // relativelayout xay dung case
        // linearlayout xay dung case
        rlXayDungCase.setPadding(chieuRong/4,chieuDai/20,chieuRong/4,(chieuDai*6)/20);
        llXayDungCase.setOrientation(LinearLayout.HORIZONTAL);
        dsAnhXayDungCase = new ImageView[kieuKhungHinh.getSoCot()][kieuKhungHinh.getSoHang()];
        click = false;

        for (int i = 0 ; i < kieuKhungHinh.getSoCot();i++){
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);
            for (int j = 0;j < kieuKhungHinh.getSoHang();j++){
                dsAnhXayDungCase[i][j] = new ImageView(this);
                dsAnhXayDungCase[i][j].setId(i*kieuKhungHinh.getSoHang() + j);
                dsAnhXayDungCase[i][j].setImageResource(R.drawable.none);
                dsAnhXayDungCase[i][j].setTag(R.drawable.none);
                dsAnhXayDungCase[i][j].setScaleType(ImageView.ScaleType.CENTER_CROP);
                dsAnhXayDungCase[i][j].setLayoutParams(new ViewGroup.LayoutParams(chieuRongCase/kieuKhungHinh.getSoCot(),chieuDaiCase/kieuKhungHinh.getSoHang()));
                row.addView(dsAnhXayDungCase[i][j]);
                final int finalI = i;
                final int finalJ = j;
                dsAnhXayDungCase[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dsAnhXayDungCase[finalI][finalJ].setBackgroundColor(getResources().getColor(R.color.mypink));
                        if(click == true) {
                            dsAnhXayDungCase[toaDoX][toaDoY].setBackgroundColor(getResources().getColor(R.color.none));
                        }
                        toaDoX = finalI;
                        toaDoY = finalJ;
                        click = true;
                    }
                });
            }
            llXayDungCase.addView(row);
        }
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            imgUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
                arrayList.add(new AnhDuocChon(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
            KhoAnhAdapter khoAnhAdapter = new KhoAnhAdapter(arrayList,getApplicationContext());
            rcAnhDuocChon.setAdapter(khoAnhAdapter);
        }
    }

    // class chon anh
    private static class recyclerViewClick implements View.OnClickListener{
        private final Context context;

        private recyclerViewClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int k = rcAnhDuocChon.getChildPosition(v);
            dsAnhXayDungCase[toaDoX][toaDoY].setImageBitmap(arrayList.get(k).getBmHinhAnh());
        }
    }

    // method random
    public static int rand(int min, int max) {
        try {
            Random rn = new Random();
            int range = max - min + 1;
            int randomNum = min + rn.nextInt(range);
            return randomNum;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
