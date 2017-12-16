package com.waterfairy.imageselect.model;

import com.waterfairy.imageselect.activity.ImageSelectActivity;
import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.bean.SearchImgBean;
import com.waterfairy.imageselect.presenter.SelectPresenterListener;
import com.waterfairy.imageselect.utils.PictureSearchTool;
import com.waterfairy.imageselect.utils.ShareTool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/12
 * @Description:
 */

public class SelectModel implements PictureSearchTool.OnSearchListener {
    private SelectPresenterListener mPresenter;
    private PictureSearchTool mPictureSearchTool;
    private ShareTool mShareTool;

    public SelectModel(SelectPresenterListener listener ) {
        this.mPresenter = listener;
        mPictureSearchTool = PictureSearchTool.getInstance();
        mPictureSearchTool.setOnSearchListener(this);
        mShareTool = ShareTool.getInstance();
    }

    public void queryFolders(int deep, boolean loadCache) {
        mPictureSearchTool.setDeep(deep);
        ArrayList<SearchFolderBean> folders = mShareTool.getFolders();
        if (folders != null && folders.size() > 0 && loadCache) {
            mPresenter.onGetFoldersSuccess(folders);
        } else {
            mPictureSearchTool.start();
        }
    }

    @Override
    public void onSearch(String path) {
        mPresenter.onSearching(path);
    }

    @Override
    public void onSearchSuccess(ArrayList<SearchFolderBean> fileList) {
//        mShareTool.saveFolder(fileList);
        mPresenter.onGetFoldersSuccess(fileList);
    }

    public void queryImgS(String path) {
        List<SearchImgBean> searchImgBeans = mPictureSearchTool.searchFolder(path);
        mPresenter.onGetImgSuccess(searchImgBeans);
    }

    public void stopSearch() {
        mPictureSearchTool.stop();
    }
}
