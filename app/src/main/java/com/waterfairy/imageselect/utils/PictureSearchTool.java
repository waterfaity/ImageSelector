package com.waterfairy.imageselect.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.bean.SearchImgBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/12/6
 * des  :
 */

public class PictureSearchTool {
    private static final String TAG = "pictureSearchTool";
    private ArrayList<SearchFolderBean> fileList = new ArrayList<>();
    private String extension[] = new String[]{".png", ".jpg"};
    private boolean running;
    private int deep = 3;
    private static final PictureSearchTool PICTURE_SEARCH_TOOL = new PictureSearchTool();
    private OnSearchListener onSearchListener;


    private PictureSearchTool() {

    }

    public static PictureSearchTool getInstance() {
        return PICTURE_SEARCH_TOOL;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public void start() {
        running = true;
        fileList.removeAll(fileList);
        startAsyncTask();

    }

    private void startAsyncTask() {
        new AsyncTask<Void, String, ArrayList<SearchFolderBean>>() {

            @Override
            protected ArrayList<SearchFolderBean> doInBackground(Void... voids) {
                search(Environment.getExternalStorageDirectory(), 0, new OnSearchListener() {
                    @Override
                    public void onSearch(String path) {
                        publishProgress(path);
                    }

                    @Override
                    public void onSearchSuccess(ArrayList<SearchFolderBean> fileList) {

                    }
                });
                return fileList;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if (onSearchListener != null) onSearchListener.onSearch(values[0]);
            }

            @Override
            protected void onPostExecute(ArrayList<SearchFolderBean> strings) {
                if (onSearchListener != null) onSearchListener.onSearchSuccess(strings);
            }
        }.execute();
    }

    public void stop() {
        running = false;
    }

    /**
     * 排除第一级文件夹:Android
     * 排除开头为.的文件夹
     *
     * @param file
     * @param deep
     * @param onSearchListener
     */
    private void search(File file, int deep, OnSearchListener onSearchListener) {
        if (file.exists() && deep < this.deep && !file.getName().startsWith(".") && !(deep == 0 && TextUtils.equals("Android", file.getName()))) {
            File[] list = file.listFiles();
            if (list != null) {
                boolean jump = false;
                for (File childFile : list) {
                    if (childFile.isDirectory()) {
                        search(childFile, deep + 1, onSearchListener);
                    } else if (!jump) {
                        String childAbsolutePath = childFile.getAbsolutePath();
                        for (String anExtension : extension) {
                            if (childAbsolutePath.endsWith(anExtension)) {
                                fileList.add(new SearchFolderBean(file.getAbsolutePath(), childAbsolutePath));
                                jump = true;
                                break;
                            }
                        }
                        if (jump) {
                            if (onSearchListener != null)
                                onSearchListener.onSearch(childAbsolutePath);
                        }
                    }
                }
            }
        }
    }

    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    public List<SearchImgBean> searchFolder(String path) {
        List<SearchImgBean> imgBeans = new ArrayList<>();
        File[] files = new File(path).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                String childPath = file.getAbsolutePath();
                for (String anExtension : extension) {
                    if (childPath.endsWith(anExtension)) {
                        imgBeans.add(new SearchImgBean(childPath, imgBeans.size()));
                    }
                }
            }
        }
        return imgBeans;
    }

    public interface OnSearchListener {
        void onSearch(String path);

        void onSearchSuccess(ArrayList<SearchFolderBean> fileList);
    }


    public boolean isRunning() {
        return isRunning();
    }
}
