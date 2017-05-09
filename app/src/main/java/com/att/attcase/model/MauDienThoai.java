package com.att.attcase.model;

/**
 * Created by mac on 5/5/17.
 */

public class MauDienThoai {
    String id;
    String name;
    String idThuongHieu;
    byte[] anh;
    byte[] anhMatSau;
    byte[] anhKhongChe;
    String gia;
    String linkAnh;
    String linkAnhMatSau;
    String linkAnhKhongChe;
    boolean checked;

    public MauDienThoai() {
        checked=false;
    }

    public MauDienThoai(String id, String name, String idThuongHieu, byte[] anh, byte[] anhMatSau, byte[] anhKhongChe, String gia, String linkAnh, String linkAnhMatSau, String linkAnhKhongChe, boolean checked) {
        this.id = id;
        this.name = name;
        this.idThuongHieu = idThuongHieu;
        this.anh = anh;
        this.anhMatSau = anhMatSau;
        this.anhKhongChe = anhKhongChe;
        this.gia = gia;
        this.linkAnh = linkAnh;
        this.linkAnhMatSau = linkAnhMatSau;
        this.linkAnhKhongChe = linkAnhKhongChe;
        this.checked = checked;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public String getLinkAnhMatSau() {
        return linkAnhMatSau;
    }

    public void setLinkAnhMatSau(String linkAnhMatSau) {
        this.linkAnhMatSau = linkAnhMatSau;
    }

    public String getLinkAnhKhongChe() {
        return linkAnhKhongChe;
    }

    public void setLinkAnhKhongChe(String linkAnhKhongChe) {
        this.linkAnhKhongChe = linkAnhKhongChe;
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

    public byte[] getAnhMatSau() {
        return anhMatSau;
    }

    public void setAnhMatSau(byte[] anhMatSau) {
        this.anhMatSau = anhMatSau;
    }

    public byte[] getAnhKhongChe() {
        return anhKhongChe;
    }

    public void setAnhKhongChe(byte[] anhKhongChe) {
        this.anhKhongChe = anhKhongChe;
    }
}
