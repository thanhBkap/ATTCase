package com.att.attcase.database;

/**
 * Created by mac on 5/5/17.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.att.attcase.model.Layout;
import com.att.attcase.model.MauDienThoai;
import com.att.attcase.model.ThuongHieu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 4/28/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "artist.sqlite";
    public static final String DB_FOLDER_PATH = "/data/data/com.att.attcase/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DB_NAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public List<ThuongHieu> getListThuongHieu() {
        ThuongHieu thuongHieu;
        List<ThuongHieu> listThuongHieu = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM thuonghieu", null);
        cursor.moveToFirst();
        thuongHieu = new ThuongHieu();
        thuongHieu.setId(cursor.getString(0));
        thuongHieu.setName(cursor.getString(1));
        thuongHieu.setAnh(cursor.getBlob(2));
        thuongHieu.setChecked(true);
        listThuongHieu.add(thuongHieu);

        while (cursor.moveToNext()) {
            thuongHieu = new ThuongHieu();
            thuongHieu.setId(cursor.getString(0));
            thuongHieu.setName(cursor.getString(1));
            thuongHieu.setAnh(cursor.getBlob(2));
            listThuongHieu.add(thuongHieu);
        }
        cursor.close();
        closeDatabase();
        return listThuongHieu;
    }

    public List<MauDienThoai> getListDienThoai(ThuongHieu thuongHieu) {
        List<MauDienThoai> listDienThoai = new ArrayList<>();
        String maThuongHieu = thuongHieu.getId();
        MauDienThoai mauDienThoai = new MauDienThoai();

        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM maudienthoai WHERE id_thuonghieu = " + maThuongHieu, null);
        cursor.moveToFirst();
        mauDienThoai.setId(cursor.getString(0));
        mauDienThoai.setName(cursor.getString(1));
        mauDienThoai.setGia(cursor.getString(2));
        mauDienThoai.setAnh(cursor.getBlob(3));
        mauDienThoai.setChecked(true);
        listDienThoai.add(mauDienThoai);

        while (cursor.moveToNext()) {
            mauDienThoai = new MauDienThoai();
            mauDienThoai.setId(cursor.getString(0));
            mauDienThoai.setName(cursor.getString(1));
            mauDienThoai.setGia(cursor.getString(2));
            mauDienThoai.setAnh(cursor.getBlob(3));
            listDienThoai.add(mauDienThoai);
        }

        cursor.close();
        closeDatabase();

        return listDienThoai;
    }

    public List<Layout> getListLayout(MauDienThoai mauDienThoai) {
        List<Layout> layoutList = new ArrayList<>();
        openDatabase();

        Cursor cursor = mDatabase.rawQuery("SELECT layout.id,layout.anh FROM layout INNER JOIN layout_maudienthoai" +
                " ON layout.id=layout_maudienthoai.id_layout" +
                " WHERE layout_maudienthoai.id_maudienthoai = " + mauDienThoai.getId(), null);
        cursor.moveToFirst();

        do {
            Layout layout = new Layout();
            layout.setId(cursor.getString(0));
            layout.setAnh(cursor.getBlob(1));
            layoutList.add(layout);
        } while (cursor.moveToNext());

        cursor.close();
        closeDatabase();
        return layoutList;

    }

    public Bitmap getAnhMatSauDienThoai(String idMauDienThoai) {
        Bitmap anhMatSauDienThoai;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT anh_mat_sau FROM maudienthoai WHERE id = " + idMauDienThoai, null);
        cursor.moveToFirst();
        byte[] mangAnh = cursor.getBlob(0);
        anhMatSauDienThoai = BitmapFactory.decodeByteArray(mangAnh, 0, mangAnh.length);
        cursor.close();
        closeDatabase();
        return anhMatSauDienThoai;
    }
    public Bitmap getAnhMatSauKhongCheDienThoai(String idMauDienThoai) {
        Bitmap anhMatSauKhongCheDienThoai;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT anh_khong_che FROM maudienthoai WHERE id = " + idMauDienThoai, null);
        cursor.moveToFirst();
        byte[] mangAnh = cursor.getBlob(0);
        anhMatSauKhongCheDienThoai = BitmapFactory.decodeByteArray(mangAnh, 0, mangAnh.length);
        cursor.close();
        closeDatabase();
        return anhMatSauKhongCheDienThoai;
    }
    public Layout getLayout(String idLayout) {
        Layout layoutDaChon = new Layout();
        openDatabase();
        Cursor cursor=mDatabase.rawQuery("SELECT so_cot,so_dong FROM layout WHERE id = "+ idLayout,null);
        cursor.moveToFirst();
        layoutDaChon.setSoCot(cursor.getInt(0));
        layoutDaChon.setSoHang(cursor.getInt(1));
        cursor.close();
        closeDatabase();
        return  layoutDaChon;
    }

    //kiểm tra database có tồn tại không
    public void checkDatabase(Context context) {
        File database = context.getApplicationContext().getDatabasePath(DatabaseHelper.DB_NAME);
        if (database.exists() == false) {
            this.getReadableDatabase();
            if (copyDatabase(context)) {
                //Toast.makeText(context.getApplicationContext(), "Tải thành công", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context.getApplicationContext(), "Tải thất bại", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DB_NAME);
            String outputFilePath = DatabaseHelper.DB_FOLDER_PATH + DatabaseHelper.DB_NAME;
            OutputStream outputStream = new FileOutputStream(outputFilePath);
            byte[] buff = new byte[1024];
            int length = 0;
            length = inputStream.read(buff);
            while (length > 0) {
                outputStream.write(buff);
                length = inputStream.read(buff);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }

    }
}
