package com.waterfairy.imageselect.bean;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/12
 * @Description:
 */

public class SearchFolderBean {
    private boolean isAll;

    public SearchFolderBean() {
    }

    public SearchFolderBean(String path, String firstImgPath) {
        this.path = path;
        this.firstImgPath = firstImgPath;
    }

    /**
     * 文件夹名字
     */
    private String name;
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

    /**
     * 子图片集合
     */
    private List<SearchImgBean> childImgBeans;

    /**
     * 文件夹名字
     *
     * @return
     */
    public String getName() {
        if (TextUtils.isEmpty(name) && !TextUtils.isEmpty(path)) {
            return new File(path).getName();
        }
        return name;
    }

    public void addChildImageBeans(List<SearchImgBean> data) {
        if (data != null && data.size() > 0) {
            if (childImgBeans == null) childImgBeans = new ArrayList<>();
            childImgBeans.addAll(data);
        }
    }

    public List<SearchImgBean> getChildImgBeans() {
        if (childImgBeans == null && !TextUtils.isEmpty(path)) {
            return null;
        }
        return childImgBeans;
    }

    public int getNum() {
        if (getChildImgBeans() != null) {
            return getChildImgBeans().size();
        } else {
            return num;
        }
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

    public void setIsAll(boolean isAll) {
        this.isAll = isAll;
        if (isAll) name = "全部图片";
    }

    public boolean isAll() {
        return isAll;
    }
}
