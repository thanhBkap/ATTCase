package com.att.attcase.model;

/**
 * Created by admin on 5/3/2017.
 */

public class MauDienThoai {
    String id;
    String name;
    String idThuongHieu;
    byte[] anh;
    String gia;
    boolean checked;

    public MauDienThoai() {
        checked=false;
    }

    public MauDienThoai(String id, String name, String idThuongHieu, byte[] anh, String gia, boolean checked) {
        this.id = id;
        this.name = name;
        this.idThuongHieu = idThuongHieu;
        this.anh = anh;
        this.gia = gia;
        this.checked = checked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdThuongHieu() {
        return idThuongHieu;
    }

    public void setIdThuongHieu(String idThuongHieu) {
        this.idThuongHieu = idThuongHieu;
    }

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
