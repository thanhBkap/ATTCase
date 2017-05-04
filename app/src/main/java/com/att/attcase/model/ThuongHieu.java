package com.att.attcase.model;

/**
 * Created by admin on 4/28/2017.
 */

public class ThuongHieu {
    String id;
    String name;
    byte[] anh;
    boolean checked;

    public ThuongHieu(String id, String name, byte[] anh, boolean checked) {
        this.id = id;
        this.name = name;
        this.anh = anh;
        this.checked = checked;
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