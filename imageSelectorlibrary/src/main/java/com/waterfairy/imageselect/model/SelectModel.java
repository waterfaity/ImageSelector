package com.waterfairy.imageselect.model;

import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.bean.SearchImgBean;
import com.waterfairy.imageselect.options.SelectImgOptions;
import com.waterfairy.imageselect.presenter.SelectPresenterListener;
import com.waterfairy.imageselect.utils.PictureSearchTool;
import com.waterfairy.imageselect.tool.ImageSelectorShareTool;

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

public class SelectModel implements PictureSearchTool.OnSearchListener {
    private SelectPresenterListener mPresenter;
    private PictureSearchTool mPictureSearchTool;
    private ImageSelectorShareTool mShareTool;
    private SelectImgOptions options;

    public SelectModel(SelectPresenterListener listener) {
        this.mPresenter = listener;
        mPictureSearchTool = PictureSearchTool.getInstance();
        mPictureSearchTool.setOnSearchListener(this);
        mShareTool = ImageSelectorShareTool.getInstance();
    }

    public void initData(SelectImgOptions options) {
        this.options = options;
        mPictureSearchTool.setDeep(options.getSearchDeep())
                .setPaths(options.getSearchPaths(),
                        options.getIgnorePaths());
    }

    public void queryFolders() {
        //设置搜索深度  指定文件夹  忽略文件夹

        //加载缓存并且缓存有存储
        if (options.isLoadCache()) {
            ArrayList<SearchFolderBean> folders = mShareTool.getFolders();
            if (folders != null && folders.size() > 0) {
                mPresenter.onGetFoldersSuccess(folders);
                return;
            }
        }
        //否则 搜索
        mPictureSearchTool.start();
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

    public void queryImgS(SearchFolderBean folderBean) {
        List<SearchImgBean> searchImgBeans = mPictureSearchTool.searchFolder(folderBean);
        mPresenter.onGetImgSuccess(searchImgBeans);
    }

    public void stopSearch() {
        mPictureSearchTool.stop();
    }

}
