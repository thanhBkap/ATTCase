package com.att.attcase.database;

/**
 * Created by mac on 5/5/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.att.attcase.model.Layout;
import com.att.attcase.model.LayoutDienThoai;
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

    public SQLiteDatabase getmDatabase() {
        return mDatabase;
    }

    public void setmDatabase(SQLiteDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

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

    //copy dữ liệu khi không có database
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

    // lấy danh sách tất cả các thương hiệu điện thoại
    public List<ThuongHieu> getListThuongHieu() {
        ThuongHieu thuongHieu;
        List<ThuongHieu> listThuongHieu = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM thuonghieu", null);
        if (cursor.moveToFirst()) {
            thuongHieu = new ThuongHieu();
            thuongHieu.setId(cursor.getString(0));
            thuongHieu.setName(cursor.getString(1));
            thuongHieu.setAnh(cursor.getBlob(2));
            thuongHieu.setChecked(true);
            listThuongHieu.add(thuongHieu);
        }
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

    // lấy danh sách tất cả các điện thoại của thương hiệu xyz
    public List<MauDienThoai> getListDienThoai(ThuongHieu thuongHieu) {
        List<MauDienThoai> listDienThoai = new ArrayList<>();
        String maThuongHieu = thuongHieu.getId();
        MauDienThoai mauDienThoai = new MauDienThoai();

        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM maudienthoai WHERE maudienthoai.id_thuonghieu = " + maThuongHieu, null);
        if (cursor.moveToFirst()) {
            mauDienThoai.setId(cursor.getString(0));
            mauDienThoai.setName(cursor.getString(1));
            mauDienThoai.setGia(cursor.getString(2));
            mauDienThoai.setAnh(cursor.getBlob(3));
            mauDienThoai.setChecked(true);
            listDienThoai.add(mauDienThoai);
        }
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

    // lấy danh sách tất cả các layout của điện thoại xyz
    public List<Layout> getListLayout(MauDienThoai mauDienThoai) {
        List<Layout> layoutList = new ArrayList<>();
        openDatabase();

        Cursor cursor = mDatabase.rawQuery("SELECT layout.id,layout.anh FROM layout INNER JOIN layout_maudienthoai" +
                " ON layout.id=layout_maudienthoai.id_layout" +
                " WHERE layout_maudienthoai.id_maudienthoai = " + mauDienThoai.getId(), null);

        if (cursor.moveToFirst()) {
            do {
                Layout layout = new Layout();
                layout.setId(cursor.getString(0));
                layout.setAnh(cursor.getBlob(1));
                layoutList.add(layout);
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDatabase();
        return layoutList;
    }

    // lấy ảnh mặt sau điện thoại dựa trên mã điện thoại
    public Bitmap getAnhMatSauDienThoai(String idMauDienThoai) {
        Bitmap anhMatSauDienThoai;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT anh_mat_sau FROM maudienthoai WHERE id = " + idMauDienThoai, null);

        if (cursor.moveToFirst()) {
            byte[] mangAnh = cursor.getBlob(0);
            anhMatSauDienThoai = BitmapFactory.decodeByteArray(mangAnh, 0, mangAnh.length);
            cursor.close();
        } else {
            anhMatSauDienThoai = null;
        }
        closeDatabase();
        return anhMatSauDienThoai;
    }

    // lấy ảnh mặt sau không che để đè lên mặt sau điện thoại dựa trên mã điện thoại
    public Bitmap getAnhMatSauKhongCheDienThoai(String idMauDienThoai) {
        Bitmap anhMatSauKhongCheDienThoai;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT anh_khong_che FROM maudienthoai WHERE id = " + idMauDienThoai, null);
        if (cursor.moveToFirst()) {
            byte[] mangAnh = cursor.getBlob(0);
            anhMatSauKhongCheDienThoai = BitmapFactory.decodeByteArray(mangAnh, 0, mangAnh.length);
            cursor.close();
        } else {
            anhMatSauKhongCheDienThoai = null;
        }
        closeDatabase();
        return anhMatSauKhongCheDienThoai;
    }

    //lấy layout tương ứng với id của layout truyền vào
    public Layout getLayout(String idLayout) {
        Layout layoutDaChon = new Layout();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT so_cot,so_dong FROM layout WHERE id = " + idLayout, null);
        cursor.moveToFirst();
        layoutDaChon.setSoCot(cursor.getInt(0));
        layoutDaChon.setSoHang(cursor.getInt(1));
        cursor.close();
        closeDatabase();
        return layoutDaChon;
    }

    public void insertDienThoai(MauDienThoai mauDienThoai) {
        ContentValues values = new ContentValues();
        values.put("id", mauDienThoai.getId());
        values.put("name", mauDienThoai.getName());
        values.put("gia", mauDienThoai.getGia());
        values.put("anh", mauDienThoai.getAnh());
        values.put("id_thuonghieu", mauDienThoai.getIdThuongHieu());
        values.put("anh_mat_sau", mauDienThoai.getAnhMatSau());
        values.put("anh_khong_che", mauDienThoai.getAnhKhongChe());
        openDatabase();
        mDatabase.insert("maudienthoai", null, values);
        closeDatabase();
    }

    public void insertThuongHieu(ThuongHieu thuongHieu) {
        ContentValues values = new ContentValues();
        values.put("id", thuongHieu.getId());
        values.put("ten", thuongHieu.getName());
        values.put("anh", thuongHieu.getAnh());
        openDatabase();
        mDatabase.insert("thuonghieu", null, values);
        closeDatabase();
    }

    public void insertLayout(Layout layout) {
        ContentValues values = new ContentValues();
        values.put("id", layout.getId());
        values.put("name", layout.getTen());
        values.put("anh", layout.getAnh());
        values.put("so_cot", layout.getSoCot());
        values.put("so_dong", layout.getSoHang());
        openDatabase();
        mDatabase.insert("layout", null, values);
        closeDatabase();
    }

    public void insertLayoutDienThoai(LayoutDienThoai layoutDienThoai) {
        ContentValues values = new ContentValues();
        values.put("id", layoutDienThoai.getId());
        values.put("id_maudienthoai", layoutDienThoai.getDienThoaiId());
        values.put("id_layout", layoutDienThoai.getLayoutId());
        openDatabase();
        mDatabase.insert("layout_maudienthoai", null, values);
        closeDatabase();
    }

    // xóa tất cả các data
    public void xoaDuLieu(Context context) {
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM layout", null);
        int i1 = mDatabase.delete("thuonghieu", "1", null);
        int i2 = mDatabase.delete("layout", "1", null);
        int i3 = mDatabase.delete("layout_maudienthoai", "1", null);
        int i4 = mDatabase.delete("maudienthoai", "1", null);
        // Toast.makeText(context, i1 + "==" + i2 + "==" + i3 + "==" + i4, Toast.LENGTH_SHORT).show();
        cursor.close();
        closeDatabase();
    }

    // lấy tất cả các data
    public void show(Context context) {
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM layout", null);
        Cursor cursor2 = mDatabase.rawQuery("SELECT * FROM thuonghieu", null);
        Cursor cursor3 = mDatabase.rawQuery("SELECT * FROM maudienthoai", null);
        Cursor cursor4 = mDatabase.rawQuery("SELECT * FROM layout_maudienthoai", null);
        //Toast.makeText(context, cursor.getCount() + "==" + cursor2.getCount() + "==" + cursor3.getCount() + "==" + cursor4.getCount(), Toast.LENGTH_SHORT).show();
        cursor.close();
        cursor2.close();
        cursor3.close();
        cursor4.close();
        closeDatabase();
    }

}
