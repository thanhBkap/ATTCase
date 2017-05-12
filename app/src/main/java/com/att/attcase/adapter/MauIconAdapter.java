package com.att.attcase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.att.attcase.R;
import com.att.attcase.XayDungCase;

/**
 * Created by mac on 5/12/17.
 */

public class MauIconAdapter extends RecyclerView.Adapter<MauIconAdapter.ViewHolder> {
    Context context;
    int[]   danhsachIcon;

    public MauIconAdapter(Context context, int[] danhsachIcon) {
        this.context = context;
        this.danhsachIcon = danhsachIcon;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.anh_duoc_chon, parent, false);
        itemView.setOnClickListener(XayDungCase.recyclerThemeClick);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imgIcon.setImageResource(danhsachIcon[position]);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return danhsachIcon.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            imgIcon = (ImageView) itemView.findViewById(R.id.img_da_chon);
        }
    }
}
