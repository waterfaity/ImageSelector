package com.waterfairy.imageselect.model;

import android.content.Context;

import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.bean.SearchImgBean;
import com.waterfairy.imageselect.options.SelectImgOptions;
import com.waterfairy.imageselect.presenter.SelectPresenterListener;
import com.waterfairy.imageselect.tool.ImageSelectorShareTool;
import com.waterfairy.imageselect.utils.PictureSearchTool;
import com.waterfairy.imageselect.utils.PictureSearchTool2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/12
 * @Description:
 */

public class SelectModel implements PictureSearchTool.OnSearchListener, PictureSearchTool2.OnSearchListener {
    private SelectPresenterListener mPresenter;
    private PictureSearchTool mPictureSearchTool;
    private PictureSearchTool2 mPictureSearchTool2;
    private SelectImgOptions options;

    public SelectModel(SelectPresenterListener listener) {
        this.mPresenter = listener;
        mPictureSearchTool = PictureSearchTool.newInstance();
        mPictureSearchTool.setOnSearchListener(this);
    }

    public SelectModel(Context context, SelectPresenterListener listener) {
        this.mPresenter = listener;
        mPictureSearchTool2 = PictureSearchTool2.newInstance(context);
        mPictureSearchTool2.setOnSearchListener(this);
    }

    public void initData(SelectImgOptions options) {
        this.options = options;
        if (mPictureSearchTool != null)
            mPictureSearchTool.setDeep(options.getSearchDeep())
                    .setPaths(options.getSearchPaths(),
                            options.getIgnorePaths()).setContainsGif(options.isContainsGif());

        if (mPictureSearchTool2 != null)
            mPictureSearchTool2.setPaths(options.getIgnorePaths()).setContainsGif(options.isContainsGif());
    }

    public void queryFolders() {
        //设置搜索深度  指定文件夹  忽略文件夹

        //加载缓存并且缓存有存储
        if (options.isLoadCache()) {
            ArrayList<SearchFolderBean> folders = ImageSelectorShareTool.getInstance().getFolders();
            if (folders != null && folders.size() > 0) {
                if (mPresenter != null)
                    mPresenter.onGetFoldersSuccess(folders);
                return;
            }
        }
        //否则 搜索
        if (mPictureSearchTool != null)
            mPictureSearchTool.start();
        else if (mPictureSearchTool2 != null)
            mPictureSearchTool2.start();
    }

    @Override
    public void onSearch(String path) {
        if (mPresenter != null)
            mPresenter.onSearching(path);
    }

    @Override
    public void onSearchSuccess(ArrayList<SearchFolderBean> fileList) {
        //保存搜索路径
        ImageSelectorShareTool.getInstance().saveFolder(fileList);
        //搜搜成功
        if (mPresenter != null)
            mPresenter.onGetFoldersSuccess(fileList);
    }

    @Override
    public void onSearchError(String errorMsg) {
    }

    public void queryImgS(SearchFolderBean folderBean) {
        List<SearchImgBean> searchImgBeans =
                mPictureSearchTool != null ? mPictureSearchTool.searchFolder(folderBean) : (mPictureSearchTool2 != null ? mPictureSearchTool2.searchFolder(folderBean) : null);
        if (mPresenter != null)
            mPresenter.onGetImgSuccess(searchImgBeans);
    }

    public void stopSearch() {
        if (mPictureSearchTool != null)
            mPictureSearchTool.stop();
        if (mPictureSearchTool2 != null)
            mPictureSearchTool2.stop();
    }

    public void onDestroy() {
        if (mPictureSearchTool != null)
            mPictureSearchTool.release();
        if (mPictureSearchTool2 != null)
            mPictureSearchTool2.release();
    }

}
