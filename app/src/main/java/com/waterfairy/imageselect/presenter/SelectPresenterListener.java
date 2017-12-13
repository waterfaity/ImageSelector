package com.waterfairy.imageselect.presenter;

import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.bean.SearchImgBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/12
 * @Description:
 */

public interface SelectPresenterListener {
    void onGetFoldersSuccess(ArrayList<SearchFolderBean> folders);

    void onGetImgSuccess(List<SearchImgBean> searchImgBeans);

    void onSearching(String path);
}
