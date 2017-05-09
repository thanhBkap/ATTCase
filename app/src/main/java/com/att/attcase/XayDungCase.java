package com.att.attcase;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.att.attcase.database.DatabaseHelper;
import com.att.attcase.kho_anh.AnhDuocChon;
import com.att.attcase.kho_anh.KhoAnhAdapter;
import com.att.attcase.model.Layout;
import com.att.attcase.xaydungcase.KieuKhungHinh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class XayDungCase extends AppCompatActivity implements android.view.View.OnClickListener,View.OnDragListener {

    //Khoi tao
    Button              btnBack,btnSave, btnMoKhoAnh, btnRefresh, btnRandom;
    Button[]            btnCacHieuUng;
    RelativeLayout      rlHieuUng, rlDanhSachAnh, rlXayDungCase;
    LinearLayout        llXayDungCase, llCongCu;
    static ImageView    img_Case;
    Uri                 imgUri;
    Bitmap              bitmap;
    KieuKhungHinh       kieuKhungHinh;
    int                 chieuDai, chieuRong, chieuDaiCase, chieuRongCase;
    String              mIdLayout;
    String              mIdMauDienThoai;
    DatabaseHelper      mDatabaseHelper;
    Layout              mLayout;
    Bitmap              mAnhMatSauDienThoai,mBitMapCase,mAnhMatSauKhongCheDienThoai;
    static int          toaDoX, toaDoY;
    ImageView           img_anh_mat_sau_khong_che,img_anh_mat_sau_che;
    KhoAnhAdapter       khoAnhAdapter;
    int                 slAnh = 0;
    private long        mDatHangClick;
    static Bitmap       bmAnhDangDung;
    private TextView    mTextMessage;
    BottomNavigationView                navigation;
    static ImageView[][]                dsAnhXayDungCase;
    static ArrayList<AnhDuocChon>       arrayList;
    private static RecyclerView         rcAnhDuocChon;
    public static View.OnTouchListener  recyclerViewTouch;
    private static final long           mDatHangXacNhan = 3500;

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
            case R.id.btn_back:
                Intent intent = new Intent(XayDungCase.this, ChonKhungLayout.class);
                startActivity(intent);
                break;

            case R.id.btn_save:
                long currentTime = System.currentTimeMillis();
                if (Math.abs(currentTime - mDatHangClick) > mDatHangXacNhan) {
                    rlXayDungCase.post(new Runnable() {
                        @Override
                        public void run() {
                            mBitMapCase = captureScreen(rlXayDungCase);
                            arrayList.add(new AnhDuocChon(mBitMapCase));
                        }
                    });
                    Toast.makeText(XayDungCase.this,"mời bạn nhấn thêm lần nữa để xác nhận đơn đặt hàng",Toast.LENGTH_SHORT).show();
                    mDatHangClick = currentTime;
                } else {
                    Intent intentDatHang = new Intent(XayDungCase.this,DatHang.class);
                    mBitMaptoByteArray(intentDatHang);
                    intentDatHang.putExtra("so_luong_anh",slAnh);
                    startActivity(intentDatHang);
                }

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
                    for (int i = 0; i < kieuKhungHinh.getSoCot(); i++) {
                        for (int j = 0; j < kieuKhungHinh.getSoHang(); j++) {
                            int rand = rand(0, arrayList.size() - 1);
                            dsAnhXayDungCase[i][j].setImageBitmap(arrayList.get(rand).getBmHinhAnh());
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn cần nhiều ảnh hơn để thực hiện chức năng này", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void addControls() {
        // Xu ly database
        Intent nhanDuLieu = getIntent();
        mIdMauDienThoai = nhanDuLieu.getStringExtra("idMauDienThoai");
        mIdLayout = nhanDuLieu.getStringExtra("idLayout");
        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.checkDatabase(this);
        mLayout = new Layout();
        mLayout=mDatabaseHelper.getLayout(mIdLayout);
        mAnhMatSauDienThoai = mDatabaseHelper.getAnhMatSauDienThoai(mIdMauDienThoai);
        img_anh_mat_sau_che = (ImageView) findViewById(R.id.img_case);
        img_anh_mat_sau_che.setImageBitmap(mAnhMatSauDienThoai);
        mAnhMatSauKhongCheDienThoai=mDatabaseHelper.getAnhMatSauKhongCheDienThoai(mIdMauDienThoai);
        img_anh_mat_sau_khong_che= (ImageView) findViewById(R.id.img_anh_mat_sau_khong_che);
        img_anh_mat_sau_khong_che.setImageBitmap(mAnhMatSauKhongCheDienThoai);
        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // list Button
        btnCacHieuUng = new Button[1];
        // Button
        btnBack = (Button) findViewById(R.id.btn_back);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnMoKhoAnh = (Button) findViewById(R.id.btn_mokhoanh);
        btnRandom = (Button) findViewById(R.id.btn_random);
        btnRefresh = (Button) findViewById(R.id.btn_refresh);
        btnCacHieuUng[0] = (Button) findViewById(R.id.btn_hieuung1);

        // Button click
        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnMoKhoAnh.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnRandom.setOnClickListener(this);
        btnCacHieuUng[0].setOnClickListener(this);

        // Recyclerview click chon anh
        recyclerViewTouch = new recyclerViewTouch(this);

        // Layout
        rlHieuUng = (RelativeLayout) findViewById(R.id.rl_thietke_hieuung);
        rlDanhSachAnh = (RelativeLayout) findViewById(R.id.rl_danhsachanh);
        rcAnhDuocChon = (RecyclerView) findViewById(R.id.rc_anhduocchon);
        llCongCu = (LinearLayout) findViewById(R.id.ll_congcu);

        //khoi tao layout
        rcAnhDuocChon.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcAnhDuocChon.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        rcAnhDuocChon.addItemDecoration(dividerItemDecoration);

        arrayList = new ArrayList<>();
        KhoAnhAdapter khoAnhAdapter = new KhoAnhAdapter(arrayList, getApplicationContext());
        rcAnhDuocChon.setAdapter(khoAnhAdapter);

        //Image
        img_Case = (ImageView) findViewById(R.id.img_case);

        //Thiet ke case
        kieuKhungHinh = new KieuKhungHinh(mLayout.getSoHang(),mLayout.getSoCot());
        rlXayDungCase = (RelativeLayout) findViewById(R.id.rl_xaydungcase);
        llXayDungCase = (LinearLayout) findViewById(R.id.ll_xaydungcase);
        //get pixel screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        chieuDai = displayMetrics.heightPixels;
        chieuRong = displayMetrics.widthPixels;
        chieuDaiCase = (chieuDai * 3) / 5;
        chieuRongCase = chieuRong / 2;

        // relativelayout xay dung case
        // linearlayout xay dung case
        rlXayDungCase.setPadding(chieuRong / 4, chieuDai / 20, chieuRong / 4, (chieuDai * 6) / 20);
        llXayDungCase.setOrientation(LinearLayout.HORIZONTAL);
        dsAnhXayDungCase = new ImageView[kieuKhungHinh.getSoCot()][kieuKhungHinh.getSoHang()];

        for (int i = 0; i < kieuKhungHinh.getSoCot(); i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.VERTICAL);
            for (int j = 0; j < kieuKhungHinh.getSoHang(); j++) {
                dsAnhXayDungCase[i][j] = new ImageView(this);
                dsAnhXayDungCase[i][j].setId(i * kieuKhungHinh.getSoHang() + j);
                dsAnhXayDungCase[i][j].setImageResource(R.drawable.none);
                dsAnhXayDungCase[i][j].setTag(R.drawable.none);
                dsAnhXayDungCase[i][j].setOnDragListener(this);
                dsAnhXayDungCase[i][j].setScaleType(ImageView.ScaleType.CENTER_CROP);
                dsAnhXayDungCase[i][j].setLayoutParams(new ViewGroup.LayoutParams(chieuRongCase/kieuKhungHinh.getSoCot(),chieuDaiCase/kieuKhungHinh.getSoHang()));
                row.addView(dsAnhXayDungCase[i][j]);
                dsAnhXayDungCase[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intentChinhSuaAnh = new Intent(XayDungCase.this,ChinhSuaAnh.class);
//                        startActivity(intentChinhSuaAnh);
                    }
                });
            }
            llXayDungCase.addView(row);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            imgUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imgUri);
                int mHeight = bitmap.getHeight();
                int mWidth = bitmap.getWidth();
                bitmap = Bitmap.createScaledBitmap(bitmap,mWidth/3,mHeight/3, true);
                arrayList.add(new AnhDuocChon(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
            khoAnhAdapter = new KhoAnhAdapter(arrayList, getApplicationContext());
            rcAnhDuocChon.setAdapter(khoAnhAdapter);
        }
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int dragEvent = event.getAction();
        final View view = (View) event.getLocalState();

        switch (dragEvent) {
            case DragEvent.ACTION_DRAG_ENTERED:
                break;

            case DragEvent.ACTION_DRAG_EXITED:
                break;

            case DragEvent.ACTION_DROP:
                ImageView imgDroped = (ImageView) v;
                imgDroped.setImageBitmap(bmAnhDangDung);
                break;
        }
        return true;
    }

    // Touch image
    private static class recyclerViewTouch implements View.OnTouchListener{

        private final Context context;

        private recyclerViewTouch(Context context) {
            this.context = context;
        }

        /**
         * Called when a touch event is dispatched to a view. This allows listeners to
         * get a chance to respond before the target view.
         *
         * @param v     The view the touch event has been dispatched to.
         * @param event The MotionEvent object containing full information about
         *              the event.
         * @return True if the listener has consumed the event, false otherwise.
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ClipData data = ClipData.newPlainText("","");
            View.DragShadowBuilder mySBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data,mySBuilder,v,0);
            int k = rcAnhDuocChon.getChildPosition(v);
            bmAnhDangDung = arrayList.get(k).getBmHinhAnh();
            return true;
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

    // capture Screen
    public static Bitmap captureScreen(View v) {
        Bitmap screenshot = null;
        try {
            if(v!=null) {
                screenshot = Bitmap.createBitmap(v.getMeasuredWidth(),v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(screenshot);
                v.draw(canvas);
            }
        }catch (Exception e){
            Log.d("ScreenShotActivity", "Failed to capture screenshot because:" + e.getMessage());
        }
        return screenshot;
    }

    // bitmap to byte array
    public void mBitMaptoByteArray(Intent intent){
        for (AnhDuocChon a : arrayList) {
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            a.getBmHinhAnh().compress(Bitmap.CompressFormat.PNG, 0, blob);
            byte[] bitmapdata = blob.toByteArray();
            intent.putExtra("anh" + slAnh, bitmapdata);
            slAnh++;
        }
    }

    // bottom navigatioon
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.btn_anh:
                    rlHieuUng.setVisibility(View.GONE);
                    rlDanhSachAnh.setVisibility(View.VISIBLE);
                    llCongCu.setVisibility(View.GONE);
                    mTextMessage.setText("Gallery");
                    return true;

                case R.id.btn_congcu:
                    rlHieuUng.setVisibility(View.GONE);
                    rlDanhSachAnh.setVisibility(View.GONE);
                    llCongCu.setVisibility(View.VISIBLE);
                    mTextMessage.setText("Tools");
                    return true;

                case R.id.btn_theme:
                    rlHieuUng.setVisibility(View.GONE);
                    rlDanhSachAnh.setVisibility(View.GONE);
                    llCongCu.setVisibility(View.GONE);
                    mTextMessage.setText("Icons");
                    return true;

                case R.id.btn_hieuung:
                    rlHieuUng.setVisibility(View.VISIBLE);
                    rlDanhSachAnh.setVisibility(View.GONE);
                    llCongCu.setVisibility(View.GONE);
                    mTextMessage.setText("Effects");
                    return true;
            }
            return false;
        }

    };
}
