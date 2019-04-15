package com.waterfairy.imageselect.options;

import android.os.Environment;
import android.text.TextUtils;

import com.waterfairy.imageselect.utils.ConstantUtils;

import java.util.ArrayList;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/1
 * @info:
 */
public class ShowImgOptions extends AOptions {

    private ArrayList<String> imgList;
    private int imgResDefault;
    private int currentPos;
    private boolean canSaveImg;
    private String saveParentPath;
    private boolean clickToDismiss;
    private boolean hasTranslateAnim = true;

    public boolean isHasTranslateAnim() {
        return hasTranslateAnim;
    }

    public ShowImgOptions setHasTranslateAnim(boolean hasTranslateAnim) {
        this.hasTranslateAnim = hasTranslateAnim;
        return this;
    }

    public ArrayList<String> getImgList() {
        return imgList;
    }

    public ShowImgOptions addImgList(ArrayList<String> imgList) {
        if (this.imgList == null) this.imgList = new ArrayList<>();
        if (imgList != null) this.imgList.addAll(imgList);
        return this;
    }

    public int getImgResDefault() {
        return imgResDefault;
    }

    public ShowImgOptions setImgResDefault(int imgResDefault) {
        this.imgResDefault = imgResDefault;
        return this;
    }

    public ShowImgOptions addImg(String path) {
        if (imgList == null) {
            imgList = new ArrayList<>();
        }
        imgList.add(path);
        return this;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public ShowImgOptions setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
        return this;
    }

    public boolean isCanSaveImg() {
        return canSaveImg;
    }

    public ShowImgOptions setCanSaveImg(boolean canSaveImg) {
        this.canSaveImg = canSaveImg;
        return this;
    }

    public String getSaveParentPath() {
        if (TextUtils.isEmpty(saveParentPath)) {
            return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        }
        return saveParentPath;
    }

    public ShowImgOptions setSaveParentPath(String saveParentPath) {
        this.saveParentPath = saveParentPath;
        return this;
    }

    public boolean isClickToDismiss() {
        return clickToDismiss;
    }

    public ShowImgOptions setClickToDismiss(boolean clickToDismiss) {
        this.clickToDismiss = clickToDismiss;
        return this;
    }

    @Override
    public ShowImgOptions setTag(String tag) {
        super.tag = tag;
        return this;
    }


    @Override
    public int getOptionsType() {
        return ConstantUtils.TYPE_SHOW;
    }

    @Override
    public int getRequestCode() {
        return ConstantUtils.REQUEST_SHOW;
    }
}
