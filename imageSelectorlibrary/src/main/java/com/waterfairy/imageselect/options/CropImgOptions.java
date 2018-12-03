package com.waterfairy.imageselect.options;

import com.waterfairy.imageselect.utils.ConstantUtils;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/3 19:31
 * @info:
 */
public class CropImgOptions implements Options {
    private String imgPath;

    @Override
    public int getType() {
        return ConstantUtils.TYPE_CROP;
    }

    @Override
    public int getRequestCode() {
        return ConstantUtils.REQUEST_CROP;
    }

    public String getImgPath() {
        return imgPath;
    }

    public CropImgOptions setImgPath(String imgPath) {
        this.imgPath = imgPath;
        return this;
    }
}
