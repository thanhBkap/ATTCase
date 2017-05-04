package com.att.attcase.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.att.attcase.R;
import com.att.attcase.model.Layout;

import java.util.List;

/**
 * Created by admin on 5/4/2017.
 */

public class LayoutAdapter extends BaseAdapter {
    Context mContext;
    List<Layout> mLayoutList;
    int mResource;

    public LayoutAdapter(Context context, List<Layout> layoutList, int resource) {
        mContext = context;
        mLayoutList = layoutList;
        mResource = resource;
    }

    @Override
    public int getCount() {
        return mLayoutList.size();
    }

    @Override
    public Object getItem(int position) {
        return mLayoutList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // chuyển đổi ảnh từ dạng byte array sang dạng bitmap
    public Bitmap getBitmapImage(int position) {
        Bitmap bitmapImage = BitmapFactory.decodeByteArray(mLayoutList.get(position).getAnh(), 0, mLayoutList.get(position).getAnh().length);
        return bitmapImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, null);
            viewHolder.img_layout = (ImageView) convertView.findViewById(R.id.img_layout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.img_layout.setImageBitmap(getBitmapImage(position));

        return convertView;
    }

    class ViewHolder {
        ImageView img_layout;
    }
}
