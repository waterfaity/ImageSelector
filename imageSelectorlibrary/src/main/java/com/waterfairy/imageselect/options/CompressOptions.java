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
    private int maxSize;
    private String compressPath;

    public int getMaxWidth() {
        return maxWidth;
    }



    /**
     * 说明:当设置了 false  width height 时 ,如果缩放比例过大 则在获取file时 降低采样率
     * >16 取4
     * >6 取2
     * 其他 取1
     *
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
}
