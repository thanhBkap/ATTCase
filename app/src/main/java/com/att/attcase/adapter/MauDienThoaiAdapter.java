package com.att.attcase.adapter;

/**
 * Created by mac on 5/5/17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.att.attcase.DatHang;
import com.att.attcase.DinhDang;
import com.att.attcase.R;
import com.att.attcase.TrangChu;
import com.att.attcase.database.DatabaseHelper;
import com.att.attcase.model.Layout;
import com.att.attcase.model.MauDienThoai;

import java.util.List;


public class MauDienThoaiAdapter extends RecyclerView.Adapter<MauDienThoaiAdapter.ViewHolder> {
    private Context mContext;
    private List<MauDienThoai> mListDienThoai;
    private Toolbar mToolbar;
    private LayoutAdapter mLayoutAdapter;
    private List<Layout> mLayoutList;

    public MauDienThoaiAdapter() {
    }

    public MauDienThoaiAdapter(Context context, List<MauDienThoai> listDienThoai, Toolbar toolbar, LayoutAdapter layoutAdapter, List<Layout> layoutList) {
        mContext = context;
        mListDienThoai = listDienThoai;
        mToolbar = toolbar;
        mLayoutAdapter = layoutAdapter;
        mLayoutList = layoutList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.recycler_view_mau_dien_thoai_item, parent, false);
        return new ViewHolder(itemView);
    }

    public MauDienThoai mauDienThoaiHienTai(int position) {
        MauDienThoai mauDienThoai = mListDienThoai.get(position);
        return mauDienThoai;
    }

    @Override
    public void onBindViewHolder(MauDienThoaiAdapter.ViewHolder holder, int position) {
        //cài đặt màu cho item được select
        if (mauDienThoaiHienTai(position).isChecked()) {
        //    holder.layout_dien_thoai_item.setBackground(mContext.getResources().getDrawable(R.drawable.vien_anh_dien_thoai));
            holder.mTxtTen.setText(mauDienThoaiHienTai(position).getName());
            holder.txt_ten_dien_thoai.setTextColor(mContext.getResources().getColor(R.color.red));
            holder.mTxtGia.setText(DinhDang.chuyenThanhDinhDangGia(mauDienThoaiHienTai(position).getGia()));
            updateLayoutList(mauDienThoaiHienTai(position));
        }else {
            holder.txt_ten_dien_thoai.setTextColor(mContext.getResources().getColor(R.color.grey));
        }
        holder.layout_dien_thoai_item.setBackgroundColor(Color.TRANSPARENT);
        holder.img_dien_thoai.setImageBitmap(getBitmapImage(position));
        holder.txt_ten_dien_thoai.setText(mauDienThoaiHienTai(position).getName());
    }

    // chuyển đổi ảnh từ dạng byte array sang dạng bitmap
    public Bitmap getBitmapImage(int position) {
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(mListDienThoai.get(position).getAnh(), 0, mListDienThoai.get(position).getAnh().length);
        return bitmapImage;
    }

    @Override
    public int getItemCount() {
        return mListDienThoai.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_dien_thoai;
        TextView txt_ten_dien_thoai;
        RelativeLayout layout_dien_thoai_item;
        TextView mTxtTen, mTxtGia;
        Button btnQuayLaiTrangChu;

        public ViewHolder(View itemView) {
            super(itemView);
            mTxtTen = (TextView) mToolbar.findViewById(R.id.txtTen);
            mTxtGia = (TextView) mToolbar.findViewById(R.id.txt_Gia);
            btnQuayLaiTrangChu = (Button) mToolbar.findViewById(R.id.btn_quay_lai);
            img_dien_thoai = (ImageView) itemView.findViewById(R.id.img_dien_thoai);
            txt_ten_dien_thoai = (TextView) itemView.findViewById(R.id.txt_ten_dien_thoai);
            layout_dien_thoai_item = (RelativeLayout) itemView.findViewById(R.id.layout_dien_thoai_item);
            getAdapterPosition();
            //hiển thị thông tin ban đầu của điện thoại ở vị trí đầu tiên
            mTxtGia.setText(DinhDang.chuyenThanhDinhDangGia(mauDienThoaiHienTai(0).getGia()));
            mTxtTen.setText(mauDienThoaiHienTai(0).getName());
            layout_dien_thoai_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MauDienThoai dienThoaiDuocChon;
                    dienThoaiDuocChon = mauDienThoaiHienTai(getAdapterPosition());
                    //lưu lại id điện thoại để sử dụng sau
                    DatHang.sDienThoaiID = dienThoaiDuocChon.getId();

                    for (MauDienThoai mauDienThoai : mListDienThoai) {
                        if (mauDienThoai.isChecked()) {
                            mauDienThoai.setChecked(false);
                            break;
                        }
                    }
                    updateLayoutList(mauDienThoaiHienTai(getAdapterPosition()));
                    mauDienThoaiHienTai(getAdapterPosition()).setChecked(true);
                    mTxtTen.setText(mauDienThoaiHienTai(getAdapterPosition()).getName());
                    DatHang.sDienThoai = mauDienThoaiHienTai(getAdapterPosition()).getName();
                    mTxtGia.setText(DinhDang.chuyenThanhDinhDangGia(mauDienThoaiHienTai(getAdapterPosition()).getGia()));
                    notifyDataSetChanged();
                }
            });
            btnQuayLaiTrangChu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent quayLaiTrangChu = new Intent(mContext, TrangChu.class);
                    quayLaiTrangChu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(quayLaiTrangChu);
                }
            });
        }
    }

    private void updateLayoutList(MauDienThoai mauDienThoai) {
        mLayoutList.clear();
        DatabaseHelper databaseHelper = new DatabaseHelper(mContext);
        databaseHelper.checkDatabase(mContext);
        mLayoutList.addAll(databaseHelper.getListLayout(mauDienThoai));
        mLayoutAdapter.notifyDataSetChanged();
    }
}
