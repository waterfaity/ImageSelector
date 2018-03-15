package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.utils.AnimUtils;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.utils.PathUtils;

import java.io.File;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageShowActivity extends AppCompatActivity {
    private boolean isVisibility = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_selector_activity_image_show);
        final PhotoView photoView = findViewById(R.id.image);
        Intent intent = getIntent();
        String url = intent.getStringExtra(ConstantUtils.STR_URL);
        String path = intent.getStringExtra(ConstantUtils.STR_PATH);
        String ori = intent.getStringExtra(ConstantUtils.SCREEN_DIRECTION);
        String title = intent.getStringExtra(ConstantUtils.STR_IMG_TITLE);
        setRequestedOrientation(TextUtils.isDigitsOnly(ori) ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : (
                TextUtils.equals(ori, ConstantUtils.SCREEN_PORT) ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE));
        TextView tVtitle = findViewById(R.id.title);
        if (!TextUtils.isEmpty(url)) {
            tVtitle.setText(TextUtils.isEmpty(title) ? PathUtils.getNameFromUrl(url) : title);
            Glide.with(this).load(url).into(photoView);
        } else if (!TextUtils.isEmpty(path)) {
            tVtitle.setText(TextUtils.isEmpty(title) ? new File(path).getName() : title);
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
