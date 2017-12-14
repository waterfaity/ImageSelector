package com.waterfairy.imageselect.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.bean.SearchImgBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/12
 * @Description:
 */

public class ShowImgAdapter extends BaseAdapter implements View.OnClickListener {
    private ArrayList<String> selectList;
    private Context mContext;
    private List<SearchImgBean> mData;
    private int mMaxNum;
    private static int mWidth;
    private OnSelectImgListener onSelectImgListener;
    private OnImgClickListener onImgClickListener;
    private Toast toast;

    public ShowImgAdapter(Context mContext, List<SearchImgBean> mData, int width, int maxNum) {
        this.mContext = mContext;
        this.mData = mData;
        this.mMaxNum = maxNum;
        this.mWidth = width;
        selectList = new ArrayList<>();
        toast = Toast.makeText(mContext, "最多只能选择" + mMaxNum + "张图片", Toast.LENGTH_SHORT);

    }

    @Override
    public int getCount() {
        if (mData != null) return mData.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mData != null) return mData.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.image_selector_item_img_show, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        SearchImgBean searchImgBean = mData.get(position);
        searchImgBean.setPos(position);
        Glide.with(mContext).load(searchImgBean.getPath()).centerCrop().into(viewHolder.imageView);
        viewHolder.rootView.setTag(searchImgBean.getPath());
        viewHolder.rootView.setOnClickListener(this);
        viewHolder.checkBox.setOnCheckedChangeListener(null);
        viewHolder.checkBox.setChecked(selectList.contains(searchImgBean.getPath()));
        viewHolder.checkBox.setTag(searchImgBean);
        viewHolder.checkBox.setOnCheckedChangeListener(getCheckListener());
        return convertView;
    }

    private CompoundButton.OnCheckedChangeListener getCheckListener() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SearchImgBean searchImgBean = (SearchImgBean) buttonView.getTag();
                if (isChecked) {
                    if (selectList.size() >= mMaxNum) {
                        //取消 选择
                        buttonView.setOnCheckedChangeListener(null);
                        buttonView.setChecked(false);
                        buttonView.setOnCheckedChangeListener(getCheckListener());
                        toast.show();
                    } else {
                        selectList.add(searchImgBean.getPath());
                        if (onSelectImgListener != null)
                            onSelectImgListener.onSelect(selectList);
                    }
                } else {
                    selectList.remove(searchImgBean.getPath());
                    if (onSelectImgListener != null)
                        onSelectImgListener.onSelect(selectList);
                }
            }
        };
    }

    public void setData(List<SearchImgBean> searchImgBeans) {
        mData = searchImgBeans;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (onImgClickListener != null)
            onImgClickListener.onClickImg((String) v.getTag());
    }

    public ArrayList<String> getSelectList() {
        return selectList;
    }


    public static class ViewHolder {
        private ImageView imageView;
        private CheckBox checkBox;
        private View rootView;

        public ViewHolder(View view) {
            rootView = view.findViewById(R.id.root_view);
            checkBox = view.findViewById(R.id.check_box);
            imageView = view.findViewById(R.id.image_view);
            ViewGroup.LayoutParams layoutParams = rootView.getLayoutParams();
            layoutParams.width = mWidth;
            layoutParams.height = mWidth;
            rootView.setLayoutParams(layoutParams);
        }
    }

    public void setOnSelectImgListener(OnSelectImgListener onSelectImgListener) {
        this.onSelectImgListener = onSelectImgListener;
    }

    public interface OnSelectImgListener {
        void onSelect(ArrayList<String> selectList);
    }

    public void setOnImgClickListener(OnImgClickListener onImgClickListener) {
        this.onImgClickListener = onImgClickListener;
    }

    public interface OnImgClickListener {
        void onClickImg(String imgPath);
    }

    public void removeAllSelect() {
        selectList.removeAll(selectList);
        notifyDataSetChanged();
    }

    public void setSelectList(ArrayList<String> dataList) {
        selectList.removeAll(selectList);
        selectList.addAll(dataList);
        notifyDataSetChanged();
    }
}
