package com.att.attcase.model;

/**
 * Created by mac on 5/5/17.
 */

public class Layout {
    String id;
    byte[] anh;
    int soHang;
    int soCot;

    public Layout() {
    }

    public Layout(String id, byte[] anh, int soHang, int soCot) {
        this.id = id;
        this.anh = anh;
        this.soHang = soHang;
        this.soCot = soCot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }

    public int getSoHang() {
        return soHang;
    }

    public void setSoHang(int soHang) {
        this.soHang = soHang;
    }

    public int getSoCot() {
        return soCot;
    }

    public void setSoCot(int soCot) {
        this.soCot = soCot;
    }
}