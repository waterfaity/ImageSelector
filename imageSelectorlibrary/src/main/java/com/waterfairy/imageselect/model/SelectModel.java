package com.waterfairy.imageselect.model;

import android.os.Bundle;

import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.bean.SearchImgBean;
import com.waterfairy.imageselect.options.SelectImgOptions;
import com.waterfairy.imageselect.presenter.SelectPresenterListener;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.utils.PictureSearchTool;
import com.waterfairy.imageselect.utils.ShareTool;

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
    private ShareTool mShareTool;
    private SelectImgOptions options;

    public SelectModel(SelectPresenterListener listener) {
        this.mPresenter = listener;
        mPictureSearchTool = PictureSearchTool.getInstance();
        mPictureSearchTool.setOnSearchListener(this);
        mShareTool = ShareTool.getInstance();
    }

    public void initData(SelectImgOptions options) {
        this.options = options;
        mPictureSearchTool.setDeep(options.getSearchDeep())
                .setPaths(options.getSearchPaths(),
                        options.getIgnorePaths());
    }

    public void queryFolders() {
        //设置搜索深度  指定文件夹  忽略文件夹
        ArrayList<SearchFolderBean> folders = mShareTool.getFolders();
        if (folders != null && folders.size() > 0 && options.isLoadCache()) {
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
        Collections.sort(searchImgBeans, new Comparator<SearchImgBean>() {
            @Override
            public int compare(SearchImgBean o1, SearchImgBean o2) {

                if (new File(o1.getPath()).lastModified() < new File(o2.getPath()).lastModified()) {
                    return 1;// 最后修改的文件在前
                } else {
                    return -1;
                }
            }
        });
        mPresenter.onGetImgSuccess(searchImgBeans);
    }

    public void stopSearch() {
        mPictureSearchTool.stop();
    }

}
