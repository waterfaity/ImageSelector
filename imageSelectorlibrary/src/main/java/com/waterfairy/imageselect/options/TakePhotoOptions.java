package com.waterfairy.imageselect.options;

import com.waterfairy.imageselect.utils.ConstantUtils;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/1
 * @info:
 */
public class TakePhotoOptions implements Options {
    private String compressPath;
    private String pathAuthority;

    public String getPathAuthority() {
        return pathAuthority;
    }

    public TakePhotoOptions setPathAuthority(String pathAuthority) {
        this.pathAuthority = pathAuthority;
        return this;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public TakePhotoOptions setCompressPath(String compressPath) {
        this.compressPath = compressPath;
        return this;
    }

    @Override
    public int getType() {
        return ConstantUtils.TYPE_TAKE_PHOTO;
    }

    @Override
    public int getRequestCode() {
        return ConstantUtils.REQUEST_TAKE_PHOTO;
    }
}
