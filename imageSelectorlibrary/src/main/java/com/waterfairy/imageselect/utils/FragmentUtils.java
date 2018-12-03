package com.waterfairy.imageselect.utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.fragment.ImageSelectFragment;
import com.waterfairy.imageselect.options.SelectImgOptions;

import java.io.Serializable;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/16
 * @Description:
 */

public class FragmentUtils {
    private static ImageSelectFragment fragment;

    public static void setFragment(AppCompatActivity activity, String screenOri) {
        if (fragment != null) {
            activity.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        fragment = new ImageSelectFragment();

        Bundle bundle = new Bundle();
        Intent intent = activity.getIntent();
        Serializable extra = intent.getSerializableExtra(ConstantUtils.OPTIONS_BEAN);
        SelectImgOptions options = null;
        if (extra != null) {
            options = (SelectImgOptions) extra;
        } else {
            options = new SelectImgOptions();
            String resultString = intent.getStringExtra(ConstantUtils.RESULT_STRING);
            if (!TextUtils.isEmpty(resultString)) {
                options.setResultString(resultString);
            }
            options.setLoadCache(intent.getBooleanExtra(ConstantUtils.LOAD_CACHE, false));
            options.setSearchPaths(intent.getStringArrayListExtra(ConstantUtils.SEARCH_PATHS));
            options.setIgnorePaths(intent.getStringArrayListExtra(ConstantUtils.IGNORE_PATHS));
            options.setMaxNum(intent.getIntExtra(ConstantUtils.MAX_NUM, ConstantUtils.DEFAULT_MAX_NUM));
            options.setSearchDeep(intent.getIntExtra(ConstantUtils.SEARCH_DEEP, ConstantUtils.DEFAULT_DEEP));
            options.setGridNum(intent.getIntExtra(ConstantUtils.GRID_NUM, ConstantUtils.DEFAULT_GRID_NUM_MIN));
        }
        bundle.putSerializable(ConstantUtils.OPTIONS_BEAN, options);
        bundle.putSerializable(ConstantUtils.OPTIONS_COMPRESS_BEAN, intent.getSerializableExtra(ConstantUtils.OPTIONS_COMPRESS_BEAN));
        fragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, fragment).commit();
    }
}
