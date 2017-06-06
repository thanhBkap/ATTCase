package com.att.attcase.adapter;

/**
 * Created by mac on 5/5/17.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.att.attcase.R;
import com.att.attcase.database.DatabaseHelper;
import com.att.attcase.model.MauDienThoai;
import com.att.attcase.model.ThuongHieu;

import java.util.List;

/**
 * Created by admin on 4/28/2017.
 */

public class ThuongHieuAdapter extends RecyclerView.Adapter<ThuongHieuAdapter.ViewHolder> {
    private Context mContext;
    private List<ThuongHieu> mListThuongHieu;
    private MauDienThoaiAdapter mMauDienThoaiAdapter;
    private List<MauDienThoai> mListDienThoai;

    public ThuongHieuAdapter() {
    }

    public ThuongHieuAdapter(Context context, List<ThuongHieu> listThuongHieu, MauDienThoaiAdapter mauDienThoaiAdapter, List<MauDienThoai> listDienThoai) {
        mContext = context;
        mListThuongHieu = listThuongHieu;
        mMauDienThoaiAdapter = mauDienThoaiAdapter;
        mListDienThoai = listDienThoai;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_view_thuong_hieu_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //cài đặt màu cho item được select
        if (mListThuongHieu.get(position).isChecked()) {
            //holder.mLayout_thuong_hieu_item.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.mLayout_thuong_hieu_item.setBackground(mContext.getResources().getDrawable(R.drawable.vien_thuong_hieu));
        } else {
            holder.mLayout_thuong_hieu_item.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        holder.img_thuong_hieu.setImageBitmap(getBitmapImage(position));
    }

    // chuyển đổi ảnh từ dạng byte array sang dạng bitmap
    public Bitmap getBitmapImage(int position) {
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(mListThuongHieu.get(position).getAnh(), 0, mListThuongHieu.get(position).getAnh().length);
        return bitmapImage;
    }

    @Override
    public int getItemCount() {
        return mListThuongHieu.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_thuong_hieu;
        RelativeLayout mLayout_thuong_hieu_item;

        public ViewHolder(View itemView) {
            super(itemView);
            img_thuong_hieu = (ImageView) itemView.findViewById(R.id.img_thuong_hieu);
            mLayout_thuong_hieu_item = (RelativeLayout) itemView.findViewById(R.id.layout_thuong_hieu_item);
            img_thuong_hieu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boTrangThaiChecked();
                    ThuongHieu thuongHieuHienTai = mListThuongHieu.get(getAdapterPosition());
                    thuongHieuHienTai.setChecked(true);
                    notifyDataSetChanged();
                    updateMauDienThoai(thuongHieuHienTai);
                }
            });
        }
    }

    private void updateMauDienThoai(ThuongHieu thuongHieu) {
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        databaseHelper.checkDatabase(mContext);
        mListDienThoai.clear();
        mListDienThoai.addAll(databaseHelper.getListDienThoai(thuongHieu));
        mMauDienThoaiAdapter.notifyDataSetChanged();
    }

    private void boTrangThaiChecked() {
        for (ThuongHieu thuongHieu : mListThuongHieu) {
            if (thuongHieu.isChecked()) {
                thuongHieu.setChecked(false);
                break;
            }
        }
    }
}