package com.att.attcase.model;

/**
 * Created by admin on 5/8/2017.
 */

public class LayoutDienThoai {
    int id;
    int layoutId;
    int dienThoaiId;

    public LayoutDienThoai(int id, int layoutId, int dienThoaiId) {
        this.id = id;
        this.layoutId = layoutId;
        this.dienThoaiId = dienThoaiId;
    }

    public LayoutDienThoai() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getDienThoaiId() {
        return dienThoaiId;
    }

    public void setDienThoaiId(int dienThoaiId) {
        this.dienThoaiId = dienThoaiId;
    }
}
