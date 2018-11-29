package com.waterfairy.imageselect.model;

import android.os.Bundle;

import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.bean.SearchImgBean;
import com.waterfairy.imageselect.presenter.SelectPresenterListener;
import com.waterfairy.imageselect.utils.ConstantUtils;
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

    private int mDeep = 3;//搜索深度 默认3层
    private ShareTool mShareTool;
    private boolean mLoadCache;

    public SelectModel(SelectPresenterListener listener) {
        this.mPresenter = listener;
        mPictureSearchTool = PictureSearchTool.getInstance();
        mPictureSearchTool.setOnSearchListener(this);
        mShareTool = ShareTool.getInstance();
    }

    public void initData(Bundle bundle) {
        if (bundle != null) {
            mLoadCache = bundle.getBoolean(ConstantUtils.LOAD_CACHE, false);
            mPictureSearchTool.setDeep(bundle.getInt(ConstantUtils.SEARCH_DEEP, mDeep))
                    .setPaths(bundle.getStringArrayList(ConstantUtils.SEARCH_PATHS),
                            bundle.getStringArrayList(ConstantUtils.IGNORE_PATHS));
        }
    }

    public void queryFolders() {
        //设置搜索深度  指定文件夹  忽略文件夹
        ArrayList<SearchFolderBean> folders = mShareTool.getFolders();
        if (folders != null && folders.size() > 0 && mLoadCache) {
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
        //保存搜索路径
        mShareTool.saveFolder(fileList);
        //搜搜成功
        mPresenter.onGetFoldersSuccess(fileList);
    }

    @Override
    public void onSearchError(String errorMsg) {
    }

    public void queryImgS(String path) {
        List<SearchImgBean> searchImgBeans = mPictureSearchTool.searchFolder(path);
        mPresenter.onGetImgSuccess(searchImgBeans);
    }

    public void stopSearch() {
        mPictureSearchTool.stop();
    }

}
