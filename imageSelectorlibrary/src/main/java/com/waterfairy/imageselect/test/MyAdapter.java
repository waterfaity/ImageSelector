package com.waterfairy.imageselect.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.waterfairy.imageselect.R;

import java.util.ArrayList;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/1
 * @info:
 */
public class MyAdapter extends BaseAdapter {
    private ArrayList<String> dataList;
    private Context context;

    public MyAdapter(ArrayList<String> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (dataList != null) return dataList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_img, parent, false);
        }
        Glide.with(context).load(dataList.get(position)).into((ImageView) convertView);
        return convertView;
    }

    public ArrayList<String> getDataList() {
        return dataList;
    }
}
