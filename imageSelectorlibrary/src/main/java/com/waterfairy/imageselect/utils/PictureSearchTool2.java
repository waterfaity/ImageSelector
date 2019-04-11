package com.waterfairy.imageselect.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.waterfairy.imageselect.bean.SearchFolderBean;
import com.waterfairy.imageselect.bean.SearchImgBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * user : water_fairy
 * email:995637517@qq.com
 * date :2017/12/6
 * des  : 原作用于搜索图片 ,修改extension  可以搜索指定的文件
 * des  : 如果该文件夹A存在符合条件的文件AB 记录该文件夹A和文件AB ,然后跳过该文件夹A搜索下一个文件夹
 * des  : 未知:file.listFile()  文件夹在前?
 */


public class PictureSearchTool2 {
    private static final String TAG = "pictureSearchTool";
    private ArrayList<SearchFolderBean> fileList = new ArrayList<>();
    //    private String extension[] = new String[]{".png", ".jpg", ".jpeg", ".PNG", ".JPEG", ".JPG"};
    private String extension[] = new String[]{".jpg", ".png", "gif"};
    private boolean running;
    private ArrayList<String> mIgnorePaths;
    private static PictureSearchTool2 PICTURE_SEARCH_TOOL;
    private OnSearchListener onSearchListener;
    private Context context;
    private final String[] projection = new String[]{
            MediaStore.Images.Media.DATA};
    private boolean containsGif;


    private PictureSearchTool2(Context context) {
        this.context = context;
    }

    public static PictureSearchTool2 newInstance(Context context) {
        if (PICTURE_SEARCH_TOOL == null) PICTURE_SEARCH_TOOL = new PictureSearchTool2(context);
        return PICTURE_SEARCH_TOOL;
    }


    //  指定文件夹  忽略文件夹
    public PictureSearchTool2 setPaths(ArrayList<String> ignorePaths) {
        this.mIgnorePaths = ignorePaths;
        return this;
    }


    public void start() {
        running = true;
        fileList.clear();
        startAsyncTask();
    }

    /**
     * 异步搜索
     */
    private void startAsyncTask() {
        new AsyncTask<Void, String, ArrayList<SearchFolderBean>>() {

            @Override
            protected ArrayList<SearchFolderBean> doInBackground(Void... voids) {


                //筛选条件
                String select = "";
                if (mIgnorePaths != null && mIgnorePaths.size() != 0) {
                    for (int i = 0; i < mIgnorePaths.size(); i++) {
                        select += (MediaStore.Files.FileColumns.DATA + " not like '" + mIgnorePaths.get(i) + "%' and ");
                    }
                }
                if (!containsGif) {
                    select += (MediaStore.Files.FileColumns.DATA + " not like '" + "%.gif' and ");
                    select += (MediaStore.Files.FileColumns.DATA + " not like '" + "%.GIF' and ");
                }
                if (!TextUtils.isEmpty(select)) {
                    select = "(" + select.substring(0, select.length() - 4) + ")";
                }
                //媒体cursor
                Cursor cursor = context.getApplicationContext().getContentResolver()
                        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                projection, select, null,
                                MediaStore.Images.Media.DATE_MODIFIED);
                //添加路径
                ArrayList<String> tempImageParentPathList = new ArrayList<>();
                ArrayList<SearchImgBean> allImageList = new ArrayList<>();
                if (cursor.moveToLast()) {
                    do {
                        String image = cursor.getString(cursor.getColumnIndex(projection[0]));
                        if (!TextUtils.isEmpty(image)) {
                            File file = new File(image);
                            String parentPath = file.getParent();
                            if (new File(image).exists()) {
                                //文件存在
                                if (!tempImageParentPathList.contains(parentPath)) {
                                    publishProgress(parentPath);
                                    //文件合并
                                    //文件件已经添加
                                    tempImageParentPathList.add(file.getParent());
                                    SearchFolderBean searchFolderBean = new SearchFolderBean(parentPath, file.getAbsolutePath());
                                    fileList.add(searchFolderBean);
                                }
                                //文件合并
                                allImageList.add(new SearchImgBean(image));
                            }
                        }
                    } while (cursor.moveToPrevious());

                }
                //合并为全部图片
                SearchFolderBean searchImgBeanAll = new SearchFolderBean();
                searchImgBeanAll.setIsAll(true);
                searchImgBeanAll.addChildImageBeans(allImageList);
                if (allImageList.size() != 0) {
                    searchImgBeanAll.setFirstImgPath(allImageList.get(0).getPath());
                }

                fileList.add(0, searchImgBeanAll);
                return fileList;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                //搜索某个文件夹
                if (onSearchListener != null) onSearchListener.onSearch(values[0]);
            }

            @Override
            protected void onPostExecute(ArrayList<SearchFolderBean> strings) {
                //搜索完毕
                if (onSearchListener != null) onSearchListener.onSearchSuccess(strings);
            }
        }.

                execute();
    }

    /**
     * 移出指定文件夹
     */
    private void removeSpePaths() {
        if (mIgnorePaths != null && mIgnorePaths.size() > 0) {
            if (fileList != null && fileList.size() > 0) {
                for (int i = 0; i < fileList.size(); i++) {
                    SearchFolderBean searchFolderBean = fileList.get(i);
                    for (int j = 0; j < mIgnorePaths.size(); j++) {
                        if (TextUtils.equals(searchFolderBean.getPath(), mIgnorePaths.get(j))) {
                            fileList.remove(searchFolderBean);
                            i--;
                        }
                    }
                }
            }
        }
    }


    public void stop() {
        running = false;
    }


    public void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    /**
     * 搜索指定文件夹Bean 获取相应资源
     * 有排序并且设置第一张图片  和图片的总数量
     *
     * @param folderBean
     * @return
     */
    public List<SearchImgBean> searchFolder(SearchFolderBean folderBean) {
        List<SearchImgBean> imgList = null;
        if (folderBean.isAll()) {
            //搜索完展示的时候会调用该方法
            imgList = folderBean.getChildImgBeans();
        } else {
            //搜索合并所有数据时以及展示时候调用
            imgList = searchFolder(folderBean.getPath());
        }
        //排序
        sortByTime(imgList);
        if (imgList != null && imgList.size() > 0) {
            folderBean.setFirstImgPath(imgList.get(0).getPath());
            folderBean.setNum(imgList.size());
        }
        return imgList;
    }

    /**
     * 指定文件夹路径搜索
     *
     * @param path
     * @return
     */
    public List<SearchImgBean> searchFolder(String path) {
        List<SearchImgBean> imgBeans = new ArrayList<>();
        File[] files = new File(path).listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    String childPath = file.getAbsolutePath();
                    for (String anExtension : extension) {
                        if (childPath.endsWith(anExtension)) {
                            imgBeans.add(new SearchImgBean(childPath));
                        }
                    }
                }
            }
        }

        return imgBeans;
    }

    /**
     * 对搜索到的文件夹下的文件时间排序
     *
     * @param imgBeans
     */
    public void sortByTime(List<SearchImgBean> imgBeans) {
        Collections.sort(imgBeans, new Comparator<SearchImgBean>() {
            @Override
            public int compare(SearchImgBean o1, SearchImgBean o2) {
                long x = new File(o1.getPath()).lastModified();
                long y = new File(o2.getPath()).lastModified();
                return (x < y) ? 1 : ((x == y) ? 0 : -1);
            }
        });
    }


    /**
     * 对搜索到的文件夹 排序
     *
     * @param fileList
     */
    private void sortByTime(ArrayList<SearchFolderBean> fileList) {
        Collections.sort(fileList, new Comparator<SearchFolderBean>() {
            @Override
            public int compare(SearchFolderBean o1, SearchFolderBean o2) {
                long x = new File(o1.getPath()).lastModified();
                long y = new File(o2.getPath()).lastModified();
                return (x < y) ? 1 : ((x == y) ? 0 : -1);
            }
        });

    }

    public PictureSearchTool2 setContainsGif(boolean containsGif) {
        this.containsGif = containsGif;
        if (containsGif) {
            extension = new String[]{".jpg", ".png", ".gif"};
        }
        return this;
    }

    public boolean getContainsGif() {
        return containsGif;
    }


    public interface OnSearchListener {
        /**
         * 搜索某个文件夹
         *
         * @param path
         */
        void onSearch(String path);

        /**
         * 搜索成功
         *
         * @param fileList
         */
        void onSearchSuccess(ArrayList<SearchFolderBean> fileList);

        /**
         * 搜索失败
         *
         * @param errorMsg
         */
        void onSearchError(String errorMsg);
    }


    public boolean isRunning() {
        return isRunning();
    }
}
