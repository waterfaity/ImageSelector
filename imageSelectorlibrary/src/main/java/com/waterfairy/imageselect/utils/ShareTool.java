package com.waterfairy.imageselect.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.waterfairy.imageselect.bean.SearchFolderBean;

import java.util.ArrayList;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/12
 * @Description:
 */

public class ShareTool {
    private static final String SHARE_NAME = "imgSelectFolder";
    private static final String SAVE_FOLDER = "imageBeanList";
    private static ShareTool SHARE_TOOL;
    private SharedPreferences share;

    private ShareTool() {

    }

    public static ShareTool getInstance() {
        if (SHARE_TOOL == null) {
            SHARE_TOOL = new ShareTool();
        }
        return SHARE_TOOL;
    }

    public void initShare(Context context) {
        if (share == null)
            share = context.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);
    }

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
}
