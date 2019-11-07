package com.waterfairy.imageselect.listener;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

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
public class GlideRequestListener implements RequestListener<Drawable> {
    private final Activity activity;
    private final boolean post;
    private View viewReferTo;
    private ImageView imageView;
    private boolean one;
    private boolean hasSet;

    public GlideRequestListener(Activity activity, View viewReferTo, ImageView imageView, boolean post) {
        this.activity = activity;
        this.imageView = imageView;
        this.viewReferTo = viewReferTo;
        this.post = post;
    }

    public GlideRequestListener setOne(boolean one) {
        this.one = one;
        return this;
    }

    @Override
    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.startPostponedEnterTransition();
        }
        return false;
    }

    @Override
    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && resource instanceof BitmapDrawable && (!one || !hasSet)) {
            hasSet = true;

            Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();

            int imgWidth = bitmap.getWidth();
            int imgHeight = bitmap.getHeight();

            int measuredHeight = viewReferTo.getMeasuredHeight();
            int measuredWidth = viewReferTo.getMeasuredWidth();

            if (imgWidth != 0 && imgHeight != 0 && measuredHeight != 0 && measuredWidth != 0) {
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                if (imgWidth / (float) (imgHeight) > measuredWidth / (float) measuredHeight) {
                    //图片宽`
                    //水平为标准
                    layoutParams.width = measuredWidth;
                    layoutParams.height = (int) (measuredWidth * imgHeight / (float) imgWidth);
                } else {
                    //垂直为标准
                    layoutParams.height = measuredHeight;
                    layoutParams.width = (int) (measuredHeight * (imgWidth / (float) imgHeight));
                }
                imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            if (post)
                activity.startPostponedEnterTransition();
        }
        return false;
    }
}
