package com.waterfairy.imageselect.bean;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/12
 * @Description:
 */

public class SearchImgBean {
    private String path;
    private int pos;

    public SearchImgBean(String path, int pos) {
        this.path = path;
        this.pos = pos;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
