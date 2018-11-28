package com.waterfairy.imageselect.utils;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/13
 * @Description:
 */

public class AnimUtils {
    public static TranslateAnimation getInAnim(boolean up, boolean in) {
        float fromValue = -1;
        float toValue = 0;
        if (up) {
            if (!in) {
                fromValue = 0;
                toValue = -1;
            }
        } else {
            if (in) {
                fromValue = 1;
                toValue = 0;
            } else {
                fromValue = 0;
                toValue = 1;
            }
        }
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 0,
                Animation.RELATIVE_TO_SELF, fromValue,
                Animation.RELATIVE_TO_SELF, toValue);
        translateAnimation.setDuration(150);
        if (!in) {
            translateAnimation.setFillAfter(true);
        }
        return translateAnimation;
    }
}
