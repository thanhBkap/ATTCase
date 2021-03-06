package com.att.attcase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;

/**
 * Created by mac on 5/5/17.
 */

public class DinhDang {
    //public static final String URL = "http://phone.websumo.vn";
    public static final String URL = "http://opdienthoai.websumo.vn";
    public static final String URL_ANH = "http://opdienthoai.websumo.vn/public";
    static int sReloadedDatabase = 0;
    public static String chuyenThanhDinhDangGia(String giaChuaDinhDang) {
        String gia = "";
        if (giaChuaDinhDang.length() > 3) {
            for (int i = 0; i < giaChuaDinhDang.length(); i++) {
                if ((giaChuaDinhDang.length() - i) == 3) {
                    gia = gia + "." + giaChuaDinhDang.charAt(i);
                } else {
                    gia = gia + giaChuaDinhDang.charAt(i);
                }
            }
        }
        return gia+" đ";
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}

