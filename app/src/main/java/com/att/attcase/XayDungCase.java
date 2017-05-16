package com.att.attcase;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.att.attcase.adapter.CropAnhAdapter;
import com.att.attcase.adapter.MauIconAdapter;
import com.att.attcase.database.DatabaseHelper;
import com.att.attcase.kho_anh.AnhDuocChon;
import com.att.attcase.kho_anh.KhoAnhAdapter;
import com.att.attcase.model.CropingOption;
import com.att.attcase.model.Layout;
import com.att.attcase.xaydungcase.KieuKhungHinh;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.widget.Toast.makeText;


public class XayDungCase extends AppCompatActivity implements android.view.View.OnClickListener, View.OnDragListener, View.OnTouchListener {

    //Khoi tao
    Uri imgUri;
    int chieuDai, chieuRong, chieuDaiCase, chieuRongCase;
    Button btnBack, btnSave, btnMoKhoAnh, btnRefresh, btnRandom;
    Bitmap mAnhMatSauDienThoai, mAnhMatSauKhongCheDienThoai, mBitMapCase;
    String mIdLayout, mIdMauDienThoai;
    Layout mLayout;
    TextView mTextMessage;
    ImageView img_anh_mat_sau_khong_che, img_anh_mat_sau_che;
    LinearLayout llXayDungCase, llCongCu;
    RelativeLayout rlHieuUng, rlDanhSachAnh, rlTheme;
    KieuKhungHinh kieuKhungHinh;
    DatabaseHelper mDatabaseHelper;
    KhoAnhAdapter khoAnhAdapter;
    ArrayList<String> dsAnhString;
    static int onTouchIndex, toadoX, toadoY, iconDuocChon = 0;
    private long mDatHangClick;
    static Uri uriAnhDangDung, getUri, caseUri;
    static ImageView img_Case;
    static ImageView[][] dsAnhXayDungCase;
    static ArrayList<AnhDuocChon> arrayList;
    private File outPutFile = null;
    private static RelativeLayout rlXayDungCase;
    private static ImageView imageNewIcon;
    private static int toaDoXicon, toaDoYicon;
    private static RecyclerView rcAnhDuocChon, rcIcon;
    public static View.OnTouchListener recyclerImageTouch;
    public static View.OnClickListener recyclerThemeClick;
    private final static int REQUEST_PERMISSION_REQ_CODE = 34;
    BottomNavigationView navigation;
    static int[] danhsachIcon = {R.drawable.ic_theme, R.drawable.ic_image, R.drawable.ic_invite2, R.drawable.ic_save, R.drawable.ic_theme,
            R.drawable.ic_theme, R.drawable.ic_theme, R.drawable.ic_theme, R.drawable.ic_theme, R.drawable.ic_theme, R.drawable.ic_theme};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xay_dung_case);
        addControls();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                Intent intent = new Intent(XayDungCase.this, ChonKhungLayout.class);
                startActivity(intent);
                break;

            case R.id.btn_save:
                long mDatHangXacNhan = 3500;
                long currentTime = System.currentTimeMillis();
                if (Math.abs(currentTime - mDatHangClick) > mDatHangXacNhan) {
                    rlXayDungCase.post(new Runnable() {
                        @Override
                        public void run() {
                            mBitMapCase = captureScreen(rlXayDungCase);
                            caseUri = getImageUri(getApplicationContext(), mBitMapCase);
                        }
                    });
                    makeText(XayDungCase.this, "mời bạn nhấn thêm lần nữa để xác nhận đơn đặt hàng", Toast.LENGTH_SHORT).show();
                    mDatHangClick = currentTime;
                } else {
                    //thểm ảnh chụp case lên đầu
                    dsAnhString.add(caseUri.toString());
                    //thêm các ảnh nhỏ
                    for (AnhDuocChon a : arrayList) {
                        dsAnhString.add(a.getUriHinhAnh().toString());
                    }

                    //chuyển sang màn hình đặt hàng
                    Intent intentDatHang = new Intent(XayDungCase.this, DatHang.class);
                    intentDatHang.putExtra("DsAnh", dsAnhString);
                    startActivity(intentDatHang);
                }

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
                            dsAnhXayDungCase[i][j].setImageURI(arrayList.get(rand).getUriHinhAnh());
                        }
                    }
                } else {
                    makeText(getApplicationContext(), "Bạn cần nhiều ảnh hơn để thực hiện chức năng này", Toast.LENGTH_SHORT).show();
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
        mLayout = mDatabaseHelper.getLayout(mIdLayout);
        mAnhMatSauDienThoai = mDatabaseHelper.getAnhMatSauDienThoai(mIdMauDienThoai);
        img_anh_mat_sau_che = (ImageView) findViewById(R.id.img_case);
        mAnhMatSauKhongCheDienThoai = mDatabaseHelper.getAnhMatSauKhongCheDienThoai(mIdMauDienThoai);
        img_anh_mat_sau_khong_che = (ImageView) findViewById(R.id.img_anh_mat_sau_khong_che);
        mTextMessage = (TextView) findViewById(R.id.message);
        img_anh_mat_sau_che.setImageBitmap(mAnhMatSauDienThoai);
        img_anh_mat_sau_khong_che.setImageBitmap(mAnhMatSauKhongCheDienThoai);

        // Bottom Navigation
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // tao duong dan cho anh crop
        outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");

        // danh sach du lieu truyen di
        dsAnhString = new ArrayList<>();

        // Button
        btnBack = (Button) findViewById(R.id.btn_back);
        btnSave = (Button) findViewById(R.id.btn_save);
        btnMoKhoAnh = (Button) findViewById(R.id.btn_mokhoanh);
        btnRandom = (Button) findViewById(R.id.btn_random);
        btnRefresh = (Button) findViewById(R.id.btn_refresh);

        // Button click
        btnBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnMoKhoAnh.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnRandom.setOnClickListener(this);

        // Recyclerview click chon anh
        recyclerImageTouch = new recyclerImageTouch(this);
        recyclerThemeClick = new recyclerThemeClick(this);

        // Layout
        rlHieuUng = (RelativeLayout) findViewById(R.id.rl_thietke_hieuung);
        rlDanhSachAnh = (RelativeLayout) findViewById(R.id.rl_danhsachanh);
        rlTheme = (RelativeLayout) findViewById(R.id.rl_theme);
        rcAnhDuocChon = (RecyclerView) findViewById(R.id.rc_anhduocchon);
        rcIcon = (RecyclerView) findViewById(R.id.rc_icon);
        llCongCu = (LinearLayout) findViewById(R.id.ll_congcu);
        rlXayDungCase = (RelativeLayout) findViewById(R.id.rl_xaydungcase);

        //khoi tao recyclelayout
        rcAnhDuocChon.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcAnhDuocChon.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        rcAnhDuocChon.addItemDecoration(dividerItemDecoration);

        rcIcon.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcIcon.setLayoutManager(layoutManager1);
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(this, layoutManager.getOrientation());
        rcIcon.addItemDecoration(dividerItemDecoration1);

        arrayList = new ArrayList<AnhDuocChon>();

        // tao cac recycler layout
        KhoAnhAdapter khoAnhAdapter = new KhoAnhAdapter(arrayList, getApplicationContext());
        rcAnhDuocChon.setAdapter(khoAnhAdapter);
        MauIconAdapter mauIconAdapter = new MauIconAdapter(getApplicationContext(), danhsachIcon);
        rcIcon.setAdapter(mauIconAdapter);

        //Image
        img_Case = (ImageView) findViewById(R.id.img_case);
        createIconButton(iconDuocChon);

        //Thiet ke case
        kieuKhungHinh = new KieuKhungHinh(mLayout.getSoHang(), mLayout.getSoCot());
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
        img_anh_mat_sau_khong_che.setPadding(chieuRong / 4, chieuDai / 20, chieuRong / 4, (chieuDai * 6) / 20);
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
                dsAnhXayDungCase[i][j].setId(-1);
                dsAnhXayDungCase[i][j].setLayoutParams(new ViewGroup.LayoutParams(chieuRongCase / kieuKhungHinh.getSoCot(), chieuDaiCase / kieuKhungHinh.getSoHang()));
                row.addView(dsAnhXayDungCase[i][j]);
                final int finalJ = j;
                final int finalI = i;
                dsAnhXayDungCase[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dsAnhXayDungCase[finalI][finalJ].getId() != -1) {
                            getUri = arrayList.get(dsAnhXayDungCase[finalI][finalJ].getId()).getUriHinhAnh();
                            toadoX = finalI;
                            toadoY = finalJ;
                            CropingIMG();
                        }
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
            arrayList.add(new AnhDuocChon(imgUri));
            khoAnhAdapter = new KhoAnhAdapter(arrayList, getApplicationContext());
            rcAnhDuocChon.setAdapter(khoAnhAdapter);
        } else if (requestCode == 1) {
            try {
                if (outPutFile.exists()) {
                    Bitmap photo = decodeFile(outPutFile);
                    dsAnhXayDungCase[toadoX][toadoY].setImageBitmap(photo);
                } else {
                    makeText(getApplicationContext(), "Error while save image", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                imgDroped.setImageURI(uriAnhDangDung);
                imgDroped.setId(onTouchIndex);
                break;
        }
        return true;
    }

    // Touch image
    private static class recyclerImageTouch implements View.OnTouchListener {

        private final Context context;

        private recyclerImageTouch(Context context) {
            this.context = context;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder mySBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, mySBuilder, v, 0);
            onTouchIndex = rcAnhDuocChon.getChildPosition(v);
            uriAnhDangDung = arrayList.get(onTouchIndex).getUriHinhAnh();
            return true;
        }
    }


    private static class recyclerThemeClick implements View.OnClickListener {
        private final Context context;

        private recyclerThemeClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            iconDuocChon = danhsachIcon[rcIcon.getChildLayoutPosition(v)];
            imageNewIcon.setVisibility(View.VISIBLE);
            imageNewIcon.setImageResource(iconDuocChon);
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
            if (v != null) {
                screenshot = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(screenshot);
                v.draw(canvas);
            }
        } catch (Exception e) {
            Log.d("ScreenShotActivity", "Failed to capture screenshot because:" + e.getMessage());
        }
        return screenshot;
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
                    rlTheme.setVisibility(View.GONE);
                    mTextMessage.setText("Gallery");
                    return true;

                case R.id.btn_congcu:
                    rlHieuUng.setVisibility(View.GONE);
                    rlDanhSachAnh.setVisibility(View.GONE);
                    llCongCu.setVisibility(View.VISIBLE);
                    rlTheme.setVisibility(View.GONE);
                    mTextMessage.setText("Tools");
                    return true;

                case R.id.btn_theme:
                    rlHieuUng.setVisibility(View.GONE);
                    rlDanhSachAnh.setVisibility(View.GONE);
                    llCongCu.setVisibility(View.GONE);
                    rlTheme.setVisibility(View.VISIBLE);
                    mTextMessage.setText("Icons");
                    return true;

                case R.id.btn_hieuung:
                    rlHieuUng.setVisibility(View.VISIBLE);
                    rlDanhSachAnh.setVisibility(View.GONE);
                    llCongCu.setVisibility(View.GONE);
                    rlTheme.setVisibility(View.GONE);
                    mTextMessage.setText("Effects");
                    Toast.makeText(getApplicationContext(), "tính năng chưa được update", Toast.LENGTH_SHORT).show();
                    return true;
            }
            return false;
        }

    };

    //crop image
    private void CropingIMG() {

        final ArrayList<CropingOption> cropOptions = new ArrayList<CropingOption>();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        int size = list.size();
        if (size == 0) {
            makeText(this, "Cann't find image croping app", Toast.LENGTH_SHORT).show();
            return;
        } else {
            intent.setData(getUri);
            intent.putExtra("outputX", 512);
            intent.putExtra("outputY", 512);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);

            //Create output file here
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outPutFile));

            if (size == 1) {
                Intent i = new Intent(intent);
                ResolveInfo res = (ResolveInfo) list.get(0);

                i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

                startActivityForResult(i, 1);
            } else {
                for (ResolveInfo res : list) {
                    final CropingOption co = new CropingOption();

                    co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
                    co.icon = getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
                    co.appIntent = new Intent(intent);
                    co.appIntent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    cropOptions.add(co);
                }

                CropAnhAdapter adapter = new CropAnhAdapter(getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Croping App");
                builder.setCancelable(false);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        startActivityForResult(cropOptions.get(item).appIntent, 1);
                    }
                });

                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                        if (getUri != null) {
                            getContentResolver().delete(getUri, null, null);
                            getUri = null;
                        }
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }


    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 512;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(XayDungCase.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_REQ_CODE);
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, final @NonNull String[] permissions, final @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQ_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                toaDoXicon = X - lParams.leftMargin;
                toaDoYicon = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = X - toaDoXicon;
                layoutParams.topMargin = Y - toaDoYicon;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                createIconButton(iconDuocChon);
                break;
        }
        rlXayDungCase.invalidate();
        return true;
    }

    private void createIconButton(int i) {
        imageNewIcon = new ImageView(this);
        imageNewIcon.setOnTouchListener(this);
        imageNewIcon.setImageResource(i);
        imageNewIcon.setVisibility(View.GONE);
        rlXayDungCase.addView(imageNewIcon);
    }
}