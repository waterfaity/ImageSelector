package com.waterfairy.imageselect.options;

import com.waterfairy.imageselect.utils.ConstantUtils;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019/4/11 17:15
 * @info:
 */
public abstract class AOptions implements Options {
    protected String tag;//tag
    protected int screenOrientation;//屏幕方向
    protected int[] transitionAnimRes;//进场动画/出场动画

    @Override
    public int getScreenOrientation() {
        if (screenOrientation == 0) return ConstantUtils.ORIENTATION_PORT;
        return screenOrientation;
    }

    @Override
    public int[] getTransitionAnimRes() {
        return transitionAnimRes;
    }

    @Override
    public String getTag() {
        return tag;
    }
}
