package com.waterfairy.imageselect.tool;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.waterfairy.imageselect.options.CompressOptions;
import com.waterfairy.imageselect.utils.ImageUtils;
import com.waterfairy.imageselect.utils.MD5Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/3 11:08
 * @info:
 */
public class CompressTool {
    private OnCompressListener onCompressListener;
    private String cachePath;
    private CompressOptions compressOptions;

    private CompressTool() {
    }

    public static CompressTool newInstance(String cachePath, CompressOptions compressOptions, OnCompressListener onCompressListener) {
        CompressTool compressTool = new CompressTool();
        compressTool.setOnCompressListener(onCompressListener);
        compressTool.setCachePath(cachePath);
        compressTool.setCompressOptions(compressOptions);
        return compressTool;
    }

    private void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }


    private void setOnCompressListener(OnCompressListener onCompressListener) {
        this.onCompressListener = onCompressListener;
    }

    public void compress(ArrayList<String> dataList) {
        if (dataList == null || dataList.size() == 0) {
            if (onCompressListener != null)
                onCompressListener.onCompressError("没有数据!");
        } else if (compressOptions == null) {
            if (onCompressListener != null)
                onCompressListener.onCompressError("compressOptions为空!");
        } else {
            //创建路径
            File rootFile = new File(cachePath);
            if (!rootFile.exists()) {
                boolean mkdirs = rootFile.mkdirs();
                if (!mkdirs) {
                    if (onCompressListener != null) {
                        onCompressListener.onCompressError("路径创建失败!");
                    }
                    return;
                }
            }
            if (rootFile.isDirectory()) {
                compressAsync(dataList);
            } else {
                if (onCompressListener != null) {
                    onCompressListener.onCompressError("目标文件非路径!");
                }
            }
        }
    }

    /**
     * 异步压缩
     *
     * @param dataList
     */
    private void compressAsync(final ArrayList<String> dataList) {
        new AsyncTask<ArrayList<String>, Integer, ArrayList<String>>() {
            @Override
            protected ArrayList<String> doInBackground(ArrayList<String>... arrayLists) {
                ArrayList<String> tempDataList = new ArrayList<>();
                for (int i = 0; i < arrayLists[0].size(); i++) {
                    publishProgress(i);
                    String sourcePath = arrayLists[0].get(i);
                    if (!TextUtils.isEmpty(sourcePath)) {
                        String endFormat = ".jpg";
                        if (sourcePath.endsWith(".png") || sourcePath.endsWith(".PNG")) {
                            endFormat = ".png";
                        }
                        //压缩保存文件
                        File file = new File(cachePath, MD5Utils.getMD5Code(sourcePath) + endFormat);
                        if (file.exists() && file.length() > 0) {
                            //已经压缩
                            tempDataList.add(file.getAbsolutePath());
                        } else {
                            //压缩
                            tempDataList.add(compress(sourcePath, file.getAbsolutePath()));
                        }
                    } else {
                        //文件名为空
                        tempDataList.add(sourcePath);
                    }
                }
                return tempDataList;
            }

            @Override
            protected void onPostExecute(ArrayList<String> strings) {
                super.onPostExecute(strings);
                //压缩完成
                if (onCompressListener != null) {
                    onCompressListener.onCompressSuccess(strings);
                }
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                if (onCompressListener != null) {
                    onCompressListener.onCompressing(values[0], dataList.size());
                }
            }
        }.execute(dataList);
    }

    /**
     * 压缩
     *
     * @param sourcePath
     * @param targetPath
     * @return
     */
    private String compress(String sourcePath, String targetPath) {
        //压缩
        Bitmap bitmap = ImageUtils.compress(new File(sourcePath), compressOptions.getMaxWidth(), compressOptions.getMaxHeight(), compressOptions.getMaxSize());
        if (bitmap == null) return sourcePath;
        //保存
        boolean b = ImageUtils.saveBitmap(targetPath, bitmap, targetPath.endsWith(".png") ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100);
        if (b) return targetPath;
        return sourcePath;
    }

    public void setCompressOptions(CompressOptions compressOptions) {
        this.compressOptions = compressOptions;
    }

    public interface OnCompressListener {
        void onCompressSuccess(ArrayList<String> tempDataList);

        void onCompressing(Integer pos, int totalSize);

        void onCompressError(String msg);
    }

}
