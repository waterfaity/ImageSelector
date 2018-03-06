package com.waterfairy.imageselect.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.bean.SearchImgBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/12/6
 * des  : 原作用于搜索图片 ,修改extension  可以搜索指定的文件
 * des  : 如果该文件夹A存在符合条件的文件AB 记录该文件夹A和文件AB ,然后跳过该文件夹A搜索下一个文件夹
 * des  : 未知:file.listFile()  文件夹在前?
 */


public class PictureSearchTool {
    private static final String TAG = "pictureSearchTool";
    private ArrayList<SearchFolderBean> fileList = new ArrayList<>();
    //    private String extension[] = new String[]{".png", ".jpg", ".jpeg", ".PNG", ".JPEG", ".JPG"};
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

    public void setExtension(String... extension) {
        this.extension = extension;
    }


    public void start() {
        if (extension == null || extension.length == 0) {
            if (onSearchListener != null) {
                onSearchListener.onSearchError("请设置搜索文件类型!");
            }
        } else if (deep <= 0) {
            if (onSearchListener != null) {
                onSearchListener.onSearchError("搜索文件层次必须大于等于1!");
            }
        } else {
            running = true;
            fileList.removeAll(fileList);
            startAsyncTask();
        }
    }

    private void startAsyncTask() {
        new AsyncTask<Void, String, ArrayList<SearchFolderBean>>() {

            @Override
            protected ArrayList<SearchFolderBean> doInBackground(Void... voids) {
                //搜索外置sd卡
                search(Environment.getExternalStorageDirectory(), 0, new OnSearchListener() {
                    @Override
                    public void onSearch(String path) {
                        publishProgress(path);
                    }

                    @Override
                    public void onSearchSuccess(ArrayList<SearchFolderBean> fileList) {

                    }

                    @Override
                    public void onSearchError(String errorMsg) {

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
        //文件夹存在 并在deep范围内
        if (file.exists() && deep < this.deep && !file.getName().startsWith(".") && !(deep == 0 && TextUtils.equals("Android", file.getName()))) {
            File[] list = file.listFiles();
            if (list != null) {
                //作为一个搜索限制 如果该文件夹A存在符合条件的文件AB 记录该文件夹A和文件AB ,然后跳过该文件夹A搜索下一个文件夹
                boolean jump = false;
                //遍历该文件夹下的所有文件及文件夹
                for (File childFile : list) {
                    if (childFile.isDirectory()) {
                        //是文件夹->继续扫描下一级文件夹
                        search(childFile, deep + 1, onSearchListener);
                    } else if (!jump) {
                        //是文件 并且不跳过
                        String childAbsolutePath = childFile.getAbsolutePath();
                        for (String anExtension : extension) {
                            if (childAbsolutePath.endsWith(anExtension)) {
                                fileList.add(new SearchFolderBean(file.getAbsolutePath(), childAbsolutePath));
                                jump = true;
                                break;
                            }
                        }
                        if (jump) {
                            //如果有跳过 说明搜索到了符合条件的文件
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

        void onSearchError(String errorMsg);
    }


    public boolean isRunning() {
        return isRunning();
    }
}
