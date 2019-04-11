package com.waterfairy.imageselect.presenter;

import android.content.Context;

import com.waterfairy.imageselect.activity.ImageSelectActivity;
import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.bean.SearchImgBean;
import com.waterfairy.imageselect.model.SelectModel;
import com.waterfairy.imageselect.options.SelectImgOptions;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.view.SelectView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/12
 * @Description:
 */

public class SelectPresenter implements SelectPresenterListener {
    private SelectView mView;
    private SelectModel mModel;

    private ArrayList<SearchFolderBean> mFolderBeans;

    public SelectPresenter(SelectView view) {
        mView = view;
    }


    public void initData(Context context, SelectImgOptions options) {
        if (options.getType() == ConstantUtils.SELECT_IMG_TYPE_SEARCH_CURSOR)
            mModel = new SelectModel(context, this);
        else
            mModel = new SelectModel(this);
        mModel.initData(options);
    }

    /**
     * 查询文件夹
     */
    public void queryFolders() {
        mModel.queryFolders();
    }

    @Override
    public void onGetFoldersSuccess(ArrayList<SearchFolderBean> folders) {
        if (!mView.isDestroy()) {
            mView.dismissDialog();
            mFolderBeans = folders;

            if (folders == null || folders.size() == 0) {
                mView.show("没有发现图片");
                return;
            }
            mView.setFolderName(0);
            queryImg(0);
        }

    }

    public void queryImg(int folderPos) {
        if (mFolderBeans != null && mFolderBeans.size() > folderPos) {
            mModel.queryImgS(mFolderBeans.get(folderPos));
        } else {
            onGetImgSuccess(null);
        }
    }

    @Override
    public void onGetImgSuccess(List<SearchImgBean> searchImgBeans) {
        mView.showImgS(searchImgBeans);
    }

    @Override
    public void onSearching(String path) {
        mView.showSearching(path);
    }

    public ArrayList<SearchFolderBean> getFolderData() {
        return mFolderBeans;
    }

    public void stopSearch() {
        mModel.stopSearch();
    }
}
