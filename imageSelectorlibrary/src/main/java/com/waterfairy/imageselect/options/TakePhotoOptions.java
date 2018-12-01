package com.waterfairy.imageselect.options;

import com.waterfairy.imageselect.utils.ConstantUtils;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/1
 * @info:
 */
public class TakePhotoOptions implements Options {
    @Override
    public int getType() {
        return ConstantUtils.TYPE_TAKE_PHOTO;
    }

    @Override
    public int getRequestCode() {
        return ConstantUtils.REQUEST_TAKE_PHOTO;
    }
}
