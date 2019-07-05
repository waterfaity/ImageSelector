package com.waterfairy.imageselect.tool;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.waterfairy.imageselect.options.CompressOptions;
import com.waterfairy.imageselect.utils.DataTransUtils;
import com.waterfairy.imageselect.utils.ImageUtils;
import com.waterfairy.imageselect.utils.MD5Utils;

import java.io.ByteArrayInputStream;
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

    /**
     * @param cachePath          缓存路径
     * @param compressOptions    压缩选项
     * @param onCompressListener 压缩监听
     * @return
     */
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

    /**
     * 开始压缩
     *
     * @param dataList
     */
    public void compress(ArrayList<String> dataList) {
        if (dataList == null || dataList.size() == 0) {
            if (onCompressListener != null)
                onCompressListener.onCompressError("没有数据!", dataList);
        } else if (compressOptions == null) {
            if (onCompressListener != null)
                onCompressListener.onCompressError("compressOptions为空!", dataList);
        } else {
            //创建路径
            File rootFile = new File(cachePath);
            if (!rootFile.exists()) {
                boolean mkdirs = rootFile.mkdirs();
                if (!mkdirs) {
                    if (onCompressListener != null) {
                        onCompressListener.onCompressError("路径创建失败!", dataList);
                    }
                    return;
                }
            }
            if (rootFile.isDirectory()) {
                compressAsync(dataList);
            } else {
                if (onCompressListener != null) {
                    onCompressListener.onCompressError("目标文件非路径!", dataList);
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
                    //当前压缩进度
                    publishProgress(i);
                    String sourcePath = arrayLists[0].get(i);
                    //判空
                    if (!TextUtils.isEmpty(sourcePath)) {
                        String compressPath = null;
                        File file = null;
                        if (sourcePath.endsWith(".gif") || sourcePath.endsWith(".GIF")) {
                            //gif  GIF 不压缩
                            compressPath = sourcePath;
                            file = new File(compressPath, sourcePath);
                        } else {
                            String cachePathName = DataTransUtils.generateFileCompressPath(compressOptions, sourcePath, MD5Utils.getMD5Code(sourcePath));
                            //压缩保存文件
                            file = new File(cachePath, cachePathName);
                            if (file.exists() && file.length() > 0) {
                                //已经压缩
                                compressPath = file.getAbsolutePath();
                            } else {
                                //压缩
                                compressPath = compress(sourcePath, file.getAbsolutePath());
                            }
                        }
                        tempDataList.add(compressPath);
                        ImageSelectorShareTool.getInstance().saveSrcPath(file.getAbsolutePath(), sourcePath);
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
     * @param sourcePath 资源陆路径
     * @param targetPath 压缩后的路径
     * @return
     */
    private String compress(String sourcePath, String targetPath) {
        //压缩 返回 bitmap / IO流
        Object object = ImageUtils.compress(new File(sourcePath), compressOptions);
        //保存图片文件
        boolean success = false;
        if (object instanceof Bitmap) {
            Bitmap bitmap = (Bitmap) object;
            success = ImageUtils.saveBitmap(targetPath, bitmap, targetPath.endsWith(".png") ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 85);
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        } else if (object instanceof ByteArrayInputStream) {
            ByteArrayInputStream byteArrayInputStream = (ByteArrayInputStream) object;
            success = ImageUtils.saveBitmap(targetPath, byteArrayInputStream);
        }
        if (success) {
            return targetPath;
        }
        return sourcePath;
    }

    public void setCompressOptions(CompressOptions compressOptions) {
        this.compressOptions = compressOptions;
    }

    public interface OnCompressListener {
        void onCompressSuccess(ArrayList<String> tempDataList);

        void onCompressing(Integer pos, int totalSize);

        void onCompressError(String msg, ArrayList<String> sourceList);
    }

}
