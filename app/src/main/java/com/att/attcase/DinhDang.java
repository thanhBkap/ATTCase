package com.att.attcase;

/**
 * Created by mac on 5/5/17.
 */

public class DinhDang {
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
}

