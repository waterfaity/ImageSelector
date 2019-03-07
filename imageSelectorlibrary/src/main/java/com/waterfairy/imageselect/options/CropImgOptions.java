package com.waterfairy.imageselect.options;

import com.waterfairy.imageselect.utils.ConstantUtils;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/3 19:31
 * @info:
 */
public class CropImgOptions implements Options {
    private String cropPath;//裁剪路径
    private String imgPath;//图片路径
    private String pathAuthority;

    private int width;
    private int height;
    private int aspectX;
    private int aspectY;

    public int getWidth() {
        return width;
    }

    public CropImgOptions setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public CropImgOptions setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getAspectX() {
        return aspectX;
    }

    public CropImgOptions setAspectX(int aspectX) {
        this.aspectX = aspectX;
        return this;
    }

    public int getAspectY() {
        return aspectY;
    }

    public CropImgOptions setAspectY(int aspectY) {
        this.aspectY = aspectY;
        return this;
    }

    public String getCropPath() {
        return cropPath;
    }

    public CropImgOptions setCropPath(String cropPath) {
        this.cropPath = cropPath;
        return this;
    }

    public String getPathAuthority() {
        return pathAuthority;
    }

    public CropImgOptions setPathAuthority(String pathAuthority) {
        this.pathAuthority = pathAuthority;
        return this;
    }

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
