package com.waterfairy.imageselect.presenter;

import android.os.Bundle;

import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.bean.SearchImgBean;
import com.waterfairy.imageselect.model.SelectModel;
import com.waterfairy.imageselect.options.SelectImgOptions;
import com.waterfairy.imageselect.view.SelectView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        mModel = new SelectModel(this);
    }


    public void initData(SelectImgOptions options) {
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
            Collections.sort(mFolderBeans, new Comparator<SearchFolderBean>() {
                @Override
                public int compare(SearchFolderBean o1, SearchFolderBean o2) {

                    if (new File(o1.getPath()).lastModified() < new File(o2.getPath()).lastModified()) {
                        return 1;// 最后修改的文件在前
                    } else {
                        return -1;
                    }
                }
            });
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
