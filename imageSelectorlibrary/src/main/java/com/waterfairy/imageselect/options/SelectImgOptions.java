package com.waterfairy.imageselect.options;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;

import com.waterfairy.imageselect.utils.ConstantUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/1
 * @info:
 */
public class SelectImgOptions implements Options, Serializable {

    private final static long serialVersionUID = 201812012107L;
     private boolean loadCache;//搜索缓存
    private String compressPath;//压缩路径
    private ArrayList<String> searchPaths;//指定搜索路径
    private ArrayList<String> ignorePaths;//忽略路径
    private int maxNum;//选择最大数 默认9
    private int searchDeep;//搜索文件夹深度 默认3
    private int gridNum;//展示grid数量 默认3

    public String getCompressPath() {
        return compressPath;
    }

    public SelectImgOptions() {
         maxNum = ConstantUtils.DEFAULT_MAX_NUM;
        searchDeep = ConstantUtils.DEFAULT_DEEP;
        gridNum = ConstantUtils.DEFAULT_GRID_NUM_MIN;
    }





    public boolean isLoadCache() {
        return loadCache;
    }

    public SelectImgOptions setLoadCache(boolean loadCache) {
        this.loadCache = loadCache;
        return this;
    }

    public ArrayList<String> getSearchPaths() {
        return searchPaths;
    }

    public SelectImgOptions setSearchPaths(ArrayList<String> searchPaths) {
        this.searchPaths = searchPaths;
        return this;
    }

    public ArrayList<String> getIgnorePaths() {
        return ignorePaths;
    }

    public SelectImgOptions setIgnorePaths(ArrayList<String> ignorePaths) {
        this.ignorePaths = ignorePaths;
        return this;
    }


    public int getMaxNum() {
        return maxNum;
    }

    public SelectImgOptions setMaxNum(int maxNum) {
        this.maxNum = maxNum;
        return this;
    }

    public int getSearchDeep() {
        if (searchDeep == 0) return ConstantUtils.DEFAULT_DEEP;
        return searchDeep;
    }

    public SelectImgOptions setSearchDeep(int searchDeep) {
        this.searchDeep = searchDeep;
        return this;
    }

    public int getGridNum(Context context) {
        if (gridNum == 0) {
            Configuration configuration = context.getResources().getConfiguration();
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                return 6;
            } else {
                return 3;
            }
        }
        return gridNum;
    }


    public SelectImgOptions setGridNum(int gridNum) {
        this.gridNum = gridNum;
        return this;
    }

    @Override
    public int getType() {
        return ConstantUtils.TYPE_SELECT;
    }

    @Override
    public int getRequestCode() {
        return ConstantUtils.REQUEST_SELECT;
    }
}
