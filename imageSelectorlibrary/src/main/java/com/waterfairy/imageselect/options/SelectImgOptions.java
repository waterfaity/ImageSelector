package com.waterfairy.imageselect.options;

import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

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
    private ArrayList<String> searchPaths;//指定搜索路径
    private ArrayList<String> ignorePaths;//忽略路径
    private boolean containsGif;
    private int maxNum;//选择最大数 默认9
    private int searchDeep;//搜索文件夹深度 默认3
    private int gridNum;//展示grid数量 默认3
    private ArrayList<String> hasSelectFiles;//已经选择的文件
    private int type;

    public boolean isContainsGif() {
        return containsGif;
    }

    public void setContainsGif(boolean containsGif) {
        this.containsGif = containsGif;
    }

    public SelectImgOptions() {
        maxNum = ConstantUtils.DEFAULT_MAX_NUM;
        searchDeep = ConstantUtils.DEFAULT_DEEP;
        gridNum = ConstantUtils.DEFAULT_GRID_NUM_MIN;
    }

    public SelectImgOptions setType(int type) {
        this.type = type;
        return this;
    }

    public boolean isLoadCache() {
        return loadCache;
    }

    /**
     * 加载缓存的搜索路径
     *
     * @param loadCache
     * @return
     */
    public SelectImgOptions setLoadCache(boolean loadCache) {
        this.loadCache = loadCache;
        return this;
    }

    public ArrayList<String> getSearchPaths() {
        return searchPaths;
    }

    /**
     * 添加额外搜索路径
     *
     * @param searchPaths
     * @return
     */
    public SelectImgOptions addSearchPaths(ArrayList<String> searchPaths) {
        if (this.searchPaths == null) this.searchPaths = new ArrayList<>();
        if (searchPaths != null) this.searchPaths.addAll(searchPaths);
        return this;
    }

    /**
     * 添加额外搜索路径
     *
     * @param searchPath
     * @return
     */
    public SelectImgOptions addSearchPath(String searchPath) {
        if (this.searchPaths == null) this.searchPaths = new ArrayList<>();
        if (!TextUtils.isEmpty(searchPath)) this.searchPaths.add(searchPath);
        return this;
    }

    /**
     * 添加忽略路径
     *
     * @param ignorePaths
     * @return
     */
    public SelectImgOptions addIgnorePaths(ArrayList<String> ignorePaths) {
        if (this.ignorePaths == null) this.ignorePaths = new ArrayList<>();
        if (ignorePaths != null) this.ignorePaths.addAll(ignorePaths);
        return this;
    }

    /**
     * 添加忽略路径
     *
     * @param ignorePath
     * @return
     */
    public SelectImgOptions addIgnorePath(String ignorePath) {
        if (this.ignorePaths == null) this.ignorePaths = new ArrayList<>();
        if (!TextUtils.isEmpty(ignorePath)) this.ignorePaths.add(ignorePath);
        return this;
    }


    public ArrayList<String> getIgnorePaths() {
        return ignorePaths;
    }

//    public SelectImgOptions addIgnorePaths(ArrayList<String> ignorePaths) {
//        this.ignorePaths = ignorePaths;
//        return this;
//    }


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

    /**
     * 添加已经选择的文件s
     *
     * @param hasSelectFiles
     */
    public SelectImgOptions addHasSelectFiles(ArrayList<String> hasSelectFiles) {
        if (this.hasSelectFiles == null)
            this.hasSelectFiles = hasSelectFiles;
        else {
            if (hasSelectFiles != null && hasSelectFiles.size() > 0) {
                this.hasSelectFiles.addAll(hasSelectFiles);
            }
        }
        return this;
    }

    public ArrayList<String> getHasSelectFiles() {
        return hasSelectFiles;
    }

    /**
     * 添加已经选择的文件
     *
     * @param hasSelectFile
     */
    public SelectImgOptions addHasSelectFile(String hasSelectFile) {
        if (!TextUtils.isEmpty(hasSelectFile)) {
            if (hasSelectFiles == null) hasSelectFiles = new ArrayList<>();
            this.hasSelectFiles.add(hasSelectFile);
        }
        return this;
    }
}
