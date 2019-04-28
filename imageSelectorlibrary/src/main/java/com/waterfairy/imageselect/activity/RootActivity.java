package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.waterfairy.imageselect.utils.ConstantUtils;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019/4/28 14:24
 * @info:
 */
public class RootActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initScreen();
    }

    private void initScreen() {
        int intExtra = getIntent().getIntExtra(ConstantUtils.SCREEN_ORIENTATION, ConstantUtils.ORIENTATION_PORT);
        if (intExtra == ConstantUtils.ORIENTATION_LAND) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        if (intent != null) {
            intent.putExtra(ConstantUtils.SCREEN_ORIENTATION, getIntent().getIntExtra(ConstantUtils.SCREEN_ORIENTATION, ConstantUtils.ORIENTATION_PORT));
        }
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivity(Intent intent) {
        if (intent != null) {
            intent.putExtra(ConstantUtils.SCREEN_ORIENTATION, getIntent().getIntExtra(ConstantUtils.SCREEN_ORIENTATION, ConstantUtils.ORIENTATION_PORT));
        }
        super.startActivity(intent);
    }
}
