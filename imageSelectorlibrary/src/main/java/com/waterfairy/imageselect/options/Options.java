package com.waterfairy.imageselect.options;

import java.io.Serializable;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/1
 * @info:
 */
public interface Options extends Serializable {
    long serialVersionUID = 201812012131L;

    int getOptionsType();

    int getRequestCode();

    int getScreenOrientation();

    String getTag();

    Options setTag(String tag);

    int[] getTransitionAnimRes();

    Options setTransitionAnimRes(int[] res);

    Options setScreenOrientation(int screenOrientation);

}
