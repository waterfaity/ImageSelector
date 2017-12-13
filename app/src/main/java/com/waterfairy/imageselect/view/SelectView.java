package com.waterfairy.imageselect.view;

import com.waterfairy.imageselect.bean.SearchImgBean;

import java.util.List;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/12
 * @Description:
 */

public interface SelectView {

    void setEnsureCanClick(boolean canClick);

    /**
     * 搜索中
     *
     * @param path
     */
    void showSearching(String path);

    void showImgS(List<SearchImgBean> searchImgBeans);

    void setFolderName(int position);

    void showSearchDialog();

    void dismissDialog();

    boolean isDestroy();
}
