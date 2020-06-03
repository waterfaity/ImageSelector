package com.waterfairy.imageselect.tool;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.utils.JsonUtils;

import java.util.ArrayList;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/12
 * @Description:
 */

public class ImageSelectorShareTool {
    public static final String SHARE_NAME = "imgSelectFolder";
    public static final String SAVE_FOLDER = "imageBeanList";
    private static ImageSelectorShareTool SHARE_TOOL;
    private SharedPreferences share;

    private ImageSelectorShareTool() {
    }

    public static ImageSelectorShareTool getInstance() {
        if (SHARE_TOOL == null) {
            SHARE_TOOL = new ImageSelectorShareTool();
        }
        return SHARE_TOOL;
    }

    public void initShare(Context context) {
        share = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 缓存 搜索到的文件夹
     *
     * @param imageBeanList
     */
    public void saveFolder(ArrayList<SearchFolderBean> imageBeanList) {
        if (share != null) {
            String saveString = "";
            if (imageBeanList != null && imageBeanList.size() > 0) {
                saveString = JsonUtils.objectToJson(imageBeanList);
            }
            share.edit().putString(SAVE_FOLDER, saveString).apply();
        } else {
            new Exception("文件选择器的SharedPreferences还未初始化").printStackTrace();
        }
    }

    /**
     * 获取缓存
     *
     * @return
     */
    public ArrayList<SearchFolderBean> getFolders() {
        if (share == null) {
            new Exception("文件选择器的SharedPreferences还未初始化").printStackTrace();
            return null;
        } else {
            String imageBeanListData = share.getString(SAVE_FOLDER, null);
            if (TextUtils.isEmpty(imageBeanListData)) {
                return null;
            } else {
                return new Gson().fromJson(imageBeanListData, new TypeToken<ArrayList<SearchFolderBean>>() {
                }.getType());
            }
        }
    }

    /**
     * 销毁
     */
    public void onDestroy() {
        if (share != null) share = null;
    }

    /**
     * 清楚缓存
     */
    public void clearCache() {
        saveFolder(null);
    }

    /**
     * 保存压缩的图片 和源图片
     *
     * @param compressTag
     * @param srcPath
     */
    public void saveSrcPath(String compressTag, String srcPath) {
        if (share != null) {
            share.edit().putString(compressTag, srcPath).apply();
        } else {
            new Exception("文件选择器的SharedPreferences还未初始化").printStackTrace();
        }
    }

    /**
     * 返回压缩前的文件路径 并且文件存在
     *
     * @param compressTag
     * @return
     */
    public String getSrcPath(String compressTag) {
        if (share != null) {
            return share.getString(compressTag, null);
        } else {
            new Exception("文件选择器的SharedPreferences还未初始化").printStackTrace();
        }
        return null;
    }
}
