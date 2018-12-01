package com.waterfairy.imageselect.options;

import android.content.Context;
import android.content.res.Configuration;

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
    private String resultString;
    private boolean loadCache;
    private ArrayList<String> searchPaths;
    private ArrayList<String> ignorePaths;
    private int maxNum;
    private int searchDeep;
    private int gridNum;

    public SelectImgOptions() {
        resultString = ConstantUtils.RESULT_STRING;
        maxNum = ConstantUtils.DEFAULT_MAX_NUM;
        searchDeep = ConstantUtils.DEFAULT_DEEP;
        gridNum = ConstantUtils.DEFAULT_GRID_NUM_MIN;
    }


    public String getResultString() {
        return resultString;
    }

    public SelectImgOptions setResultString(String resultString) {
        this.resultString = resultString;
        return this;
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
