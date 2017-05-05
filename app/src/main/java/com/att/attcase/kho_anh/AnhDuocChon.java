package com.att.attcase.kho_anh;

import android.graphics.Bitmap;

/**
 * Created by mac on 4/28/17.
 */

public class AnhDuocChon {
    private Bitmap bmHinhAnh;

    public AnhDuocChon(Bitmap bmHinhAnh) {
        this.bmHinhAnh = bmHinhAnh;
    }

    public Bitmap getBmHinhAnh() {
        return bmHinhAnh;
    }

    public void setBmHinhAnh(Bitmap bmHinhAnh) {
        this.bmHinhAnh = bmHinhAnh;
    }
}
