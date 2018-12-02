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
public class ShowImgOptions implements Options {


//    Intent intent = getIntent();
//    dataList =intent.getStringArrayListExtra(ConstantUtils.DATA_LIST);
//    mResImgDefault =intent.getIntExtra(ConstantUtils.DEFAULT_IMG_RES,0);
//    mCurrentPos =intent.getIntExtra(ConstantUtils.CURRENT_POS,0);
//    mCanSaveImg =intent.getBooleanExtra(ConstantUtils.CAN_SAVE_IMG,false);
//    mParentPath =intent.getStringExtra(ConstantUtils.SAVE_PARENT_PATH);
//    mClickToDismiss =intent.getBooleanExtra(ConstantUtils.CLICK_TO_DISMISS,false);
//        if(TextUtils.isEmpty(mParentPath))
//
//    {
//        mParentPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
//    }


    private ArrayList<String> imgList;
    private int imgResDefault;
    private int currentPos;
    private boolean canSaveImg;
    private String saveParentPath;
    private boolean clickToDismiss;

    public ArrayList<String> getImgList() {
        return imgList;
    }

    public ShowImgOptions setImgList(ArrayList<String> imgList) {
        this.imgList = imgList;
        return this;
    }

    public int getImgResDefault() {
        return imgResDefault;
    }

    public ShowImgOptions setImgResDefault(int imgResDefault) {
        this.imgResDefault = imgResDefault;
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
    public int getType() {
        return ConstantUtils.TYPE_SHOW;
    }

    @Override
    public int getRequestCode() {
        return ConstantUtils.REQUEST_SHOW;
    }
}