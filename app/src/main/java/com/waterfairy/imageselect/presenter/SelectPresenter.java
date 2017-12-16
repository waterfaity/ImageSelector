package com.waterfairy.imageselect.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.waterfairy.imageselect.activity.ImageSelectActivity;
import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.bean.SearchImgBean;
import com.waterfairy.imageselect.model.SelectModel;
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
    private int mDeep = 3;//搜索深度 默认3层
    private ArrayList<String> mIgnorePaths;//忽略的文件夹
    private ArrayList<String> mSearchPaths;//指定搜索文件夹(必须是忽略文件夹中的文件夹)

    private ArrayList<SearchFolderBean> mFolderBeans;

    public SelectPresenter(SelectView view) {
        mView = view;
        mModel = new SelectModel(this);
    }

    public void getExtra(Intent intent) {
        mDeep = intent.getIntExtra(ConstantUtils.SEARCH_DEEP, mDeep);
        mIgnorePaths = intent.getStringArrayListExtra(ConstantUtils.IGNORE_PATHS);
        mSearchPaths = intent.getStringArrayListExtra(ConstantUtils.SEARCH_PATHS);
    }

    public void getExtraBundle(Bundle bundle) {
        mDeep = bundle.getInt(ConstantUtils.SEARCH_DEEP, mDeep);
        mIgnorePaths = bundle.getStringArrayList(ConstantUtils.IGNORE_PATHS);
        mSearchPaths = bundle.getStringArrayList(ConstantUtils.SEARCH_PATHS);
    }

    /**
     * 查询文件夹
     */
    public void queryFolders() {
        mModel.queryFolders(mDeep, false);
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
            mModel.queryImgS(mFolderBeans.get(folderPos).getPath());
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
