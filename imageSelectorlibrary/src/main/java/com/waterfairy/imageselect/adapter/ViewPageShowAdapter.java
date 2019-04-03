package com.waterfairy.imageselect.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.waterfairy.imageselect.R;

import java.util.ArrayList;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/11/30 15:59
 * @info:
 */
public class ViewPageShowAdapter extends PagerAdapter {
    private Activity activity;
    private ArrayList<String> dataList;

    private int mResImgDefault;
    private OnViewClickListener onClickListener;

    public ViewPageShowAdapter setResImgDefault(int resImgDefault) {
        this.mResImgDefault = resImgDefault;
        return this;
    }

    public ViewPageShowAdapter(Activity activity, ArrayList<String> dataList) {
        this.activity = activity;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final View view = LayoutInflater.from(activity).inflate(R.layout.image_selector_img, container, false);
        container.addView(view);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) onClickListener.onViewClick();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) onClickListener.onViewClick();
            }
        });
        imageView.setTag(R.id.key_glide, dataList.get(position));
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onViewOnLongClick((ImageView) v, (String) v.getTag(R.id.key_glide));
                }
                return false;
            }
        });
        Glide.with(activity).load(dataList.get(position)).placeholder(mResImgDefault).error(mResImgDefault).into(imageView);
        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public ViewPageShowAdapter setOnClickListener(OnViewClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public interface OnViewClickListener {
        void onViewClick();

        void onViewOnLongClick(ImageView imageView, String imgPath);
    }

}
