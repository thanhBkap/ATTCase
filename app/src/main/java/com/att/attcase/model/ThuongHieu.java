package com.att.attcase.model;

/**
 * Created by mac on 5/5/17.
 */

public class ThuongHieu {
    String id;
    String name;
    byte[] anh;
    String linkAnh;
    boolean checked;

    public ThuongHieu(String id, String name, byte[] anh, String linkAnh, boolean checked) {
        this.id = id;
        this.name = name;
        this.anh = anh;
        this.linkAnh = linkAnh;
        this.checked = checked;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public ThuongHieu() {
        this.checked = false;
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

    public byte[] getAnh() {
        return anh;
    }

    public void setAnh(byte[] anh) {
        this.anh = anh;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
