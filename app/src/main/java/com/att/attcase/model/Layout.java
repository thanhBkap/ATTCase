package com.att.attcase.model;

/**
 * Created by admin on 5/4/2017.
 */

public class Layout {
    String id;
    byte[] anh;

    public Layout() {
    }

    public Layout(String id, byte[] anh) {
        this.id = id;
        this.anh = anh;
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
}
