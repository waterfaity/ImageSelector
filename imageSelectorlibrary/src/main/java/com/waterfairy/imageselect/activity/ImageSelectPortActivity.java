package com.waterfairy.imageselect.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.utils.FragmentUtils;

public class ImageSelectPortActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select_land);
        FragmentUtils.setFragment(this, ConstantUtils.SCREEN_PORT);
    }
}
