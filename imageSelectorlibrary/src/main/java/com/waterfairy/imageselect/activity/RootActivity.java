package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.waterfairy.imageselect.utils.ConstantUtils;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019/4/28 14:24
 * @info:
 */
public class RootActivity extends AppCompatActivity {
    private int[] transitionAnimRes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initScreen();
    }

    private void initScreen() {
        int intExtra = getIntent().getIntExtra(ConstantUtils.SCREEN_ORIENTATION, ConstantUtils.ORIENTATION_PORT);
        if (intExtra == ConstantUtils.ORIENTATION_LAND) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (intExtra == ConstantUtils.ORIENTATION_PORT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        transitionAnimRes = getIntent().getIntArrayExtra(ConstantUtils.TRANSITION_RES);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent != null) {
            intent.putExtra(ConstantUtils.SCREEN_ORIENTATION, getIntent().getIntExtra(ConstantUtils.SCREEN_ORIENTATION, ConstantUtils.ORIENTATION_PORT));
        }
        super.startActivityForResult(intent, requestCode);
        if (transitionAnimRes != null) {
            overridePendingTransition(transitionAnimRes[0], transitionAnimRes[1]);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (intent != null) {
            intent.putExtra(ConstantUtils.SCREEN_ORIENTATION, getIntent().getIntExtra(ConstantUtils.SCREEN_ORIENTATION, ConstantUtils.ORIENTATION_PORT));
        }
        super.startActivity(intent);
        if (transitionAnimRes != null) {
            overridePendingTransition(transitionAnimRes[0], transitionAnimRes[1]);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (transitionAnimRes != null) {
            overridePendingTransition(transitionAnimRes[2], transitionAnimRes[3]);
        }
    }
}
