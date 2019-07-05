package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.utils.AnimUtils;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.utils.PathUtils;
import com.waterfairy.imageselect.widget.ZoomImageView;

import java.io.File;

//import com.github.chrisbanes.photoview.OnPhotoTapListener;
//import com.github.chrisbanes.photoview.PhotoView;


public class ImageShowActivity extends RootActivity {
    private boolean isVisibility = true;
    private ZoomImageView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_selector_activity_image_show);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        photoView = findViewById(R.id.image);
        photoView.setCanZoom(false);
        Intent intent = getIntent();
        //url
        String url = intent.getStringExtra(ConstantUtils.STR_URL);
        //本地地址
        String path = intent.getStringExtra(ConstantUtils.STR_PATH);
        //标题
        String title = intent.getStringExtra(ConstantUtils.STR_IMG_TITLE);  //设置标题
        DrawableTransitionOptions drawableTransitionOptions = DrawableTransitionOptions.withCrossFade();

        TextView tVTitle = findViewById(R.id.title);
        if (!TextUtils.isEmpty(url)) {
            tVTitle.setText(TextUtils.isEmpty(title) ? PathUtils.getNameFromUrl(url) : title);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                photoView.setTransitionName(url);
            }
            Glide.with(this).load(url).
                    transition(drawableTransitionOptions).listener(requestListener).into(photoView);
        } else if (!TextUtils.isEmpty(path)) {
            tVTitle.setText(TextUtils.isEmpty(title) ? new File(path).getName() : title);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                photoView.setTransitionName(path);
            }
            Glide.with(this).load(new File(path)).
                    transition(drawableTransitionOptions).listener(requestListener).into(photoView);
        }
        final View topView = findViewById(R.id.rel_top);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisibility) {
                    topView.startAnimation(AnimUtils.getInAnim(true, false));
                } else {
                    topView.startAnimation(AnimUtils.getInAnim(true, true));
                }
                isVisibility = !isVisibility;
            }
        });
//        scheduleStartPostponedTransition(photoView);
    }

    private RequestListener<Drawable> requestListener = new RequestListener<Drawable>() {
        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startPostponedEnterTransition();
            }
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && resource instanceof BitmapDrawable) {

                Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();

                int imgWidth = bitmap.getWidth();
                int imgHeight = bitmap.getHeight();

                View rootView = findViewById(R.id.root_view);
                int measuredHeight = rootView.getMeasuredHeight();
                int measuredWidth = rootView.getMeasuredWidth();

                if (imgWidth != 0 && imgHeight != 0 && measuredHeight != 0 && measuredWidth != 0) {
                    ViewGroup.LayoutParams layoutParams = photoView.getLayoutParams();
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
                    photoView.setLayoutParams(layoutParams);
                    photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }

                startPostponedEnterTransition();
            }
            return false;
        }
    };

    private void scheduleStartPostponedTransition(final View sharedElement) {
//        sharedElement.getViewTreeObserver().addOnPreDrawListener(
//                new ViewTreeObserver.OnPreDrawListener() {
//                    @Override
//                    public boolean onPreDraw() {
//                        //启动动画
//                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                            startPostponedEnterTransition();
//                        }
//                        return true;
//                    }
//                });
    }


    public void back(View view) {
        if (isVisibility)
            ActivityCompat.finishAfterTransition(this);
    }
}
