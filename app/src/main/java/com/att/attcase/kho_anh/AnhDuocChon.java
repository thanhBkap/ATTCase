package com.att.attcase.kho_anh;

import android.net.Uri;

import java.io.Serializable;

/**
 * Created by mac on 4/28/17.
 */

public class AnhDuocChon implements Serializable{
    private Uri uriHinhAnh;

    public Uri getUriHinhAnh() {
        return uriHinhAnh;
    }

    public void setUriHinhAnh(Uri uriHinhAnh) {
        this.uriHinhAnh = uriHinhAnh;
    }

    public AnhDuocChon(Uri uriHinhAnh) {
        this.uriHinhAnh = uriHinhAnh;
    }
}