package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.utils.AnimUtils;

import java.io.File;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageShowActivity extends AppCompatActivity {
    private boolean isVisibility = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show);
        final PhotoView photoView = findViewById(R.id.image);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String path = intent.getStringExtra("path");
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this).load(url).into(photoView);
        } else if (!TextUtils.isEmpty(path)) {
            Glide.with(this).load(new File(path)).into(photoView);
        }
        final View topView = findViewById(R.id.rel_top);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {

                if (isVisibility) {
                    topView.startAnimation(AnimUtils.getInAnim(true, false));
                } else {
                    topView.startAnimation(AnimUtils.getInAnim(true, true));
                }
                isVisibility = !isVisibility;
            }
        });

    }

    public void back(View view) {
        if (isVisibility)
            finish();
    }
}
