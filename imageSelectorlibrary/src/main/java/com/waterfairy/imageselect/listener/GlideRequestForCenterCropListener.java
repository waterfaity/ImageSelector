package com.waterfairy.imageselect.listener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/11/30 09:51
 * @info:
 */
public class GlideRequestForCenterCropListener implements RequestListener<Drawable> {
    private ImageView imageView;

    public GlideRequestForCenterCropListener(ImageView imageView) {
        this.imageView = imageView;
    }


    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return false;
    }
}
