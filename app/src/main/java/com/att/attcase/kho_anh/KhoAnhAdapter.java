package com.att.attcase.kho_anh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.att.attcase.R;
import com.att.attcase.XayDungCase;

import java.util.ArrayList;

/**
 * Created by mac on 4/28/17.
 */

public class KhoAnhAdapter extends RecyclerView.Adapter<KhoAnhAdapter.ViewHolder> {

    ArrayList<AnhDuocChon> data;
    Context context;

    public KhoAnhAdapter(ArrayList<AnhDuocChon> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.anh_duoc_chon,parent,false);
        itemView.setOnTouchListener(XayDungCase.recyclerViewTouch);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imgHinh.setImageURI(data.get(position).getUriHinhAnh());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgHinh;
        public ViewHolder(View itemView) {
            super(itemView);
            imgHinh = (ImageView) itemView.findViewById(R.id.img_da_chon);
            imgHinh.setTag(R.drawable.case3);
        }
    }
}
