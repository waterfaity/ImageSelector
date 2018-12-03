package com.waterfairy.imageselect;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.waterfairy.imageselect.activity.ImageSelectActivity;
import com.waterfairy.imageselect.activity.ImageViewPagerShowActivity;
import com.waterfairy.imageselect.options.CompressOptions;
import com.waterfairy.imageselect.options.Options;
import com.waterfairy.imageselect.utils.ConstantUtils;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/1
 * @info:
 */
public class ImageSelector {

    private Activity activity;
    private Options options;
    private CompressOptions compressOptions;

    private ImageSelector() {
    }

    private ImageSelector(Activity activity) {
        this.activity = activity;
    }

    public static ImageSelector with(Activity activity) {
        return new ImageSelector(activity);
    }

    public ImageSelector options(Options options) {
        this.options = options;
        return this;
    }

    /**
     * 获取intent  附带跳转class
     *
     * @return
     */
    public Intent intent() {
        if (options == null) {
            new Exception("请添加options").printStackTrace();
            return new Intent();
        } else {
            Class aClass = null;
            switch (options.getType()) {
                case ConstantUtils.TYPE_SELECT:
                    aClass = ImageSelectActivity.class;
                    break;
                case ConstantUtils.TYPE_SHOW:
                    aClass = ImageViewPagerShowActivity.class;
                    break;
                case ConstantUtils.TYPE_TAKE_PHOTO:
                    break;

            }
            Intent intent = new Intent(activity, aClass);
            intent.putExtra(ConstantUtils.OPTIONS_BEAN, options);
            intent.putExtra(ConstantUtils.OPTIONS_COMPRESS_BEAN, compressOptions);
            return intent;
        }
    }

    /**
     * 展示图片
     *
     * @param view
     * @param transitionName
     * @param requestCode
     */
    public void showImg(View view, String transitionName, int requestCode) {
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view, transitionName);
        ActivityCompat.startActivityForResult(activity, intent(), requestCode, activityOptionsCompat.toBundle());
    }

    /**
     * 展示图片
     *
     * @param view
     * @param transitionName
     */
    public void showImg(View view, String transitionName) {
        if (options == null) {
            new Exception("请添加options").printStackTrace();
        } else {
            showImg(view, transitionName, options.getRequestCode());
        }
    }

    /**
     * 选择图片
     */
    public void selectImg() {
        if (options == null) {
            new Exception("请添加options").printStackTrace();
        } else {
            selectImg(options.getRequestCode());
        }
    }

    /**
     * 选择图片
     *
     * @param requestCode
     */
    public void selectImg(int requestCode) {
        if (options == null) {
            new Exception("请添加options").printStackTrace();
        } else {
            if (activity == null) {
                new Exception("请设置activity").printStackTrace();
            } else {
                activity.startActivityForResult(intent(), requestCode);
            }
        }
    }

    public ImageSelector compress(CompressOptions compressOptions) {
        this.compressOptions = compressOptions;
        return this;
    }

}
