package com.waterfairy.imageselect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.utils.PathUtils;

import java.util.ArrayList;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/12
 * @Description:
 */

public class ShowFolderAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SearchFolderBean> mData;
    private RadioButton lastRadioButton;
    private int lastPos;
    private OnClickFolderListener onClickFolderListener;

    public ShowFolderAdapter(Context mContext, ArrayList<SearchFolderBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        if (mData != null) return mData.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mData != null && mData.size() > position) return mData.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_folder_list, parent, false);
            convertView.setTag(new  ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        SearchFolderBean searchFolderBean = mData.get(position);
        viewHolder.folderName.setText(PathUtils.getNameFromUrl(searchFolderBean.getPath()));
        Glide.with(mContext).load(searchFolderBean.getFirstImgPath()).centerCrop().into(viewHolder.imageView);
        if (lastPos == position) {
            viewHolder.radioButton.setVisibility(View.VISIBLE);
            lastRadioButton=viewHolder.radioButton;
        }else {
            viewHolder.radioButton.setVisibility(View.GONE);
        }
        viewHolder.rootView.setTag(viewHolder.radioButton);
        viewHolder.radioButton.setTag(position);
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton radioButton = (RadioButton) v.getTag();
                int position = (int) radioButton.getTag();
                if (radioButton != lastRadioButton) {
                    lastRadioButton.setVisibility(View.GONE);
                    lastPos = position;
                    lastRadioButton = radioButton;
                    lastRadioButton.setVisibility(View.VISIBLE);
                    if (onClickFolderListener != null)
                        onClickFolderListener.onClickFolder(position);
                }
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        private RadioButton radioButton;
        private TextView folderName;
        private TextView folderImgNum;
        private ImageView imageView;
        private View rootView;

        public ViewHolder(View view) {
            rootView = view.findViewById(R.id.root_view);
            imageView = view.findViewById(R.id.image_view);
            folderName = view.findViewById(R.id.folder_name);
            folderImgNum = view.findViewById(R.id.folder_img_num);
            this.radioButton = view.findViewById(R.id.radio);
        }
    }

    public void setOnClickFolderListener(OnClickFolderListener onClickFolderListener) {
        this.onClickFolderListener = onClickFolderListener;
    }

    public interface OnClickFolderListener {
        void onClickFolder(int position);
    }
}
