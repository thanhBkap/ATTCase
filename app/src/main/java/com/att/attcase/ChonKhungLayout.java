package com.att.attcase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.att.attcase.adapter.LayoutAdapter;
import com.att.attcase.adapter.MauDienThoaiAdapter;
import com.att.attcase.adapter.ThuongHieuAdapter;
import com.att.attcase.database.DatabaseHelper;
import com.att.attcase.model.Layout;
import com.att.attcase.model.MauDienThoai;
import com.att.attcase.model.ThuongHieu;

import java.util.ArrayList;
import java.util.List;

public class ChonKhungLayout extends AppCompatActivity {
    private DatabaseHelper mDatabaseHelper;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerViewThuongHieu, mRecyclerViewMauDienThoai;
    private GridView mGridViewLayout;
    private LayoutAdapter mLayoutAdapter;
    private ThuongHieuAdapter mThuongHieuAdapter;
    private MauDienThoaiAdapter mMauDienThoaiAdapter;
    private List<ThuongHieu> mListThuongHieu;
    private List<MauDienThoai> mListMauDienThoai;
    private List<Layout> mListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_khung_layout);
        addControls();
        addEvents();
    }

    private void addControls() {
        //thiết lập action bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //khởi tạo các giá trị cho biến
        mDatabaseHelper = new DatabaseHelper(this);
        mListThuongHieu = new ArrayList<>();
        mListMauDienThoai = new ArrayList<>();
        mListLayout = new ArrayList<>();

        mDatabaseHelper.show(ChonKhungLayout.this);
        mRecyclerViewThuongHieu = (RecyclerView) findViewById(R.id.recycler_view_thuong_hieu);
        mRecyclerViewMauDienThoai = (RecyclerView) findViewById(R.id.recycler_view_mau_dien_thoai);
        mGridViewLayout = (GridView) findViewById(R.id.gv_layout);
        //thiết lập cho recycler view
        mRecyclerViewThuongHieu.setHasFixedSize(true);
        mRecyclerViewMauDienThoai.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerViewThuongHieu.setLayoutManager(linearLayoutManager);
        mRecyclerViewMauDienThoai.setLayoutManager(linearLayoutManager2);

        mLayoutAdapter = new LayoutAdapter(this,mListLayout,R.layout.grid_layout_item);
        mMauDienThoaiAdapter = new MauDienThoaiAdapter(this, mListMauDienThoai, mToolbar,mLayoutAdapter,mListLayout);
        mThuongHieuAdapter = new ThuongHieuAdapter(this, mListThuongHieu, mMauDienThoaiAdapter, mListMauDienThoai);

        mRecyclerViewThuongHieu.setAdapter(mThuongHieuAdapter);
        mRecyclerViewMauDienThoai.setAdapter(mMauDienThoaiAdapter);
        mGridViewLayout.setAdapter(mLayoutAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewThuongHieu.getContext(),
                DividerItemDecoration.HORIZONTAL);

        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));

        mRecyclerViewThuongHieu.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewMauDienThoai.setItemAnimator(new DefaultItemAnimator());

        mDatabaseHelper.checkDatabase(ChonKhungLayout.this);
        mListThuongHieu.clear();
        // cập nhật dữ liệu khởi tạo ban đầu cho list thương hiệu, list điện thoại cho thương hiệu đầu,
        // list layout cho điện thoại đầu
        mListThuongHieu.addAll(mDatabaseHelper.getListThuongHieu());
        mListMauDienThoai.clear();
        if (mListThuongHieu.size()>0){
            mListMauDienThoai.addAll(mDatabaseHelper.getListDienThoai(mListThuongHieu.get(0)));
            DatHang.sDienThoaiID=mListMauDienThoai.get(0).getId();
        }
        mListLayout.clear();
        if (mListMauDienThoai.size()>0){
            mListLayout.addAll(mDatabaseHelper.getListLayout(mListMauDienThoai.get(0)));
            DatHang.sLayoutID=mListLayout.get(0).getId();
        }
        DatHang.sDienThoai="Iphone 5";
        mThuongHieuAdapter.notifyDataSetChanged();
        mMauDienThoaiAdapter.notifyDataSetChanged();
        mLayoutAdapter.notifyDataSetChanged();
    }

    private void addEvents() {
        mGridViewLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Layout layoutDuocChon= (Layout) parent.getItemAtPosition(position);
                DatHang.sLayoutID=layoutDuocChon.getId();
                Intent chuyenSangTrangChinhSuaCase = new Intent(ChonKhungLayout.this,XayDungCase.class);
                chuyenSangTrangChinhSuaCase.putExtra("idMauDienThoai",layDienThoaiDaChon());
                chuyenSangTrangChinhSuaCase.putExtra("idLayout",mListLayout.get(position).getId());
                startActivity(chuyenSangTrangChinhSuaCase);
            }
        });
    }

    public String layDienThoaiDaChon(){
        String id="0";
        for (int i=0;i<mListMauDienThoai.size();i++){
            if (mListMauDienThoai.get(i).isChecked()){
                id=mListMauDienThoai.get(i).getId();
                break;
            }
        }
        return id;
    }
}

