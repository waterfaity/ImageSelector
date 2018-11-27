package com.waterfairy.imageselect.bean;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/12
 * @Description:
 */

public class SearchFolderBean {
    public SearchFolderBean(String path, String firstImgPath) {
        this.path = path;
        this.firstImgPath = firstImgPath;
    }

    public SearchFolderBean(String path, String firstImgPath, int num) {
        this.path = path;
        this.firstImgPath = firstImgPath;
        this.num = num;
    }

    /**
     * 当前有图片的文件夹的路径
     */
    private String path;
    /**
     * 第一张图片地址
     */
    private String firstImgPath;
    /**
     * 图片数量
     */
    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFirstImgPath() {
        return firstImgPath;
    }


    public void setFirstImgPath(String firstImgPath) {

        this.firstImgPath = firstImgPath;
    }

}
