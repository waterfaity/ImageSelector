package com.waterfairy.imageselect.options;

import java.io.Serializable;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/3 10:37
 * @info:
 */
public class CompressOptions implements Serializable {
    private static final long serialVersionUID = 20181203124520L;
    private int maxWidth = -1;
    private int maxHeight = -1;
    private int maxSize;//针对jpg
    private String compressPath;//压缩保存路径
    private boolean hasExtension = true;//保存是否保留后缀
    private boolean formatToJpg = false;//png格式转jpg  注:透明背景会被处理成黑色
    private boolean rotateAble;//是否可以旋转 设置targetDegree 则 =true
    private int targetDegree;//目标角度 (部分手机拍照会旋转)

    public boolean hasExtension() {
        return hasExtension;
    }

    public void setHasExtension(boolean hasExtension) {
        this.hasExtension = hasExtension;
    }

    public int getMaxWidth() {
        return maxWidth;
    }


    public int getTargetDegree() {
        return targetDegree;
    }

    public CompressOptions setTargetDegree(int targetDegree) {
        this.targetDegree = targetDegree;
        rotateAble = true;
        return this;
    }

    public CompressOptions setRotate(boolean rotateAble, int targetDegree) {
        this.rotateAble = rotateAble;
        this.targetDegree = targetDegree;
        return this;
    }

    /**
     * 说明:当设置了 false  width height 时 ,如果缩放比例过大 则在获取file时 降低采样率
     * >16 取4
     * >6 取2
     * 其他 取1
     */
    public CompressOptions setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
        return this;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public CompressOptions setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    public boolean isFormatToJpg() {
        return formatToJpg;
    }

    public CompressOptions setFormatToJpg(boolean formatToJpg) {
        this.formatToJpg = formatToJpg;
        return this;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public CompressOptions setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public CompressOptions setCompressPath(String compressPath) {
        this.compressPath = compressPath;
        return this;
    }

    public boolean getRotateAble() {
        return rotateAble;
    }

    public CompressOptions setRotateAble(boolean rotateAble) {
        this.rotateAble = rotateAble;
        return this;
    }
}
