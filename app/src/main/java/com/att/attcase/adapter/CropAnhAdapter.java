package com.att.attcase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.att.attcase.R;
import com.att.attcase.model.CropingOption;

import java.util.ArrayList;

/**
 * Created by mac on 5/11/17.
 */

public class CropAnhAdapter extends ArrayAdapter {
    private ArrayList mOptions;
    private LayoutInflater mInflater;

    public CropAnhAdapter(Context context, ArrayList options) {
        super(context, R.layout.activity_crop, options);

        mOptions  = options;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.activity_crop, null);

        CropingOption item = (CropingOption) mOptions.get(position);

        if (item != null) {
            ((ImageView) convertView.findViewById(R.id.img_icon)).setImageDrawable(item.icon);
            ((TextView) convertView.findViewById(R.id.txt_name)).setText(item.title);

            return convertView;
        }

        return null;
    }
}
