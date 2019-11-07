package com.waterfairy.imageselect.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.listener.GlideRequestListener;
import com.waterfairy.imageselect.widget.ZoomImageView;

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
    private int mCurrentPos;
    private View mReferToView;
    private OnViewClickListener onClickListener;
    private boolean hasTranslateAnim;

    public ViewPageShowAdapter setReferToView(View view) {
        mReferToView = view;
        return this;
    }

    public ViewPageShowAdapter setResImgDefault(int resImgDefault) {
        this.mResImgDefault = resImgDefault;
        return this;
    }

    public ViewPageShowAdapter setCurrentPos(int currentPos) {
        this.mCurrentPos = currentPos;
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
        ZoomImageView imageView = view.findViewById(R.id.img);
        imageView.setCanZoom(!hasTranslateAnim);
        showView(imageView, position);
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
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onViewOnLongClick((ImageView) v, (String) v.getTag(R.id.key_glide));
                }
                return false;
            }
        });
        imageView.setTag(R.id.key_glide, dataList.get(position));
        return view;
    }

    /**
     * 展示单个imageView
     *
     * @param imageView
     * @param position
     */
    private void showView(ImageView imageView, int position) {
        if (hasTranslateAnim) {
            RequestBuilder<Drawable> load = Glide.with(activity).load(dataList.get(position));
            if (mResImgDefault != 0) {
                load = load.apply(new RequestOptions().placeholder(mResImgDefault).error(mResImgDefault));
            }
            if (mCurrentPos == position) {
                load = load.listener(new GlideRequestListener(activity, mReferToView, imageView, true).setOne(true));
            } else {
                load = load.listener(new GlideRequestListener(activity, mReferToView, imageView, false).setOne(true));
            }
            load.into(imageView);
        } else {
            Glide.with(activity).load(dataList.get(position)).apply(new RequestOptions().error(mResImgDefault).placeholder(mResImgDefault)).into(imageView);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public ViewPageShowAdapter setOnClickListener(OnViewClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    public ViewPageShowAdapter setHasTranslateAnim(boolean hasTranslateAnim) {
        this.hasTranslateAnim = hasTranslateAnim;
        return this;
    }

    public boolean getHasTranslateAnim() {
        return hasTranslateAnim;
    }

    public interface OnViewClickListener {
        void onViewClick();

        void onViewOnLongClick(ImageView imageView, String imgPath);
    }

}
