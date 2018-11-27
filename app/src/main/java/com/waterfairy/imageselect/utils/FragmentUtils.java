package com.waterfairy.imageselect.utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.fragment.ImageSelectFragment;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/16
 * @Description:
 */

public class FragmentUtils {
    private static ImageSelectFragment fragment;

    public static void serFragment(AppCompatActivity activity, String screenOri) {
        if (fragment != null) {
            activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        fragment = new ImageSelectFragment();
        Bundle bundle = new Bundle();
        Intent intent = activity.getIntent();
        bundle.putString(ConstantUtils.RESULT_STRING, intent.getStringExtra(ConstantUtils.RESULT_STRING));
        bundle.putBoolean(ConstantUtils.LOAD_CACHE, intent.getBooleanExtra(ConstantUtils.LOAD_CACHE,false));
        bundle.putStringArrayList(ConstantUtils.SEARCH_PATHS, intent.getStringArrayListExtra(ConstantUtils.SEARCH_PATHS));
        bundle.putStringArrayList(ConstantUtils.IGNORE_PATHS, intent.getStringArrayListExtra(ConstantUtils.IGNORE_PATHS));
        String screenOriFromIntent = intent.getStringExtra(ConstantUtils.SCREEN_DIRECTION);
        String endDir = TextUtils.isEmpty(screenOriFromIntent) ? screenOri : screenOriFromIntent;
        bundle.putString(ConstantUtils.SCREEN_DIRECTION, endDir);
        bundle.putInt(ConstantUtils.MAX_NUM, intent.getIntExtra(ConstantUtils.MAX_NUM, ConstantUtils.DEFAULT_MAX_NUM));
        bundle.putInt(ConstantUtils.SEARCH_DEEP, intent.getIntExtra(ConstantUtils.SEARCH_DEEP, ConstantUtils.DEFAULT_DEEP));
        bundle.putInt(ConstantUtils.GRID_NUM, intent.getIntExtra(ConstantUtils.GRID_NUM,
                TextUtils.equals(endDir, ConstantUtils.SCREEN_PORT) ? ConstantUtils.DEFAULT_GRID_NUM_MIN : ConstantUtils.DEFAULT_GRID_NUM_MAX));
        fragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragment).commit();
    }
}
