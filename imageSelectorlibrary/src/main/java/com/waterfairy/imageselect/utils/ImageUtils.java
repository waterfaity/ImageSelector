package com.waterfairy.imageselect.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/11/30 17:07
 * @info:
 */
public class ImageUtils {
    /**
     * 图片保存
     *
     * @param imgPath        保存路径
     * @param source         需要保存的img
     * @param compressFormat 格式
     * @param quality        质量 1-100
     * @return
     */
    public static boolean saveBitmap(String imgPath, Bitmap source, Bitmap.CompressFormat compressFormat, int quality) {

        if (source == null || source.isRecycled()) return false;
        if (TextUtils.isEmpty(imgPath)) return false;
        File file = new File(imgPath);
        boolean canSave = true;
        if (!file.exists()) {
            File parent = file.getParentFile();
            canSave = parent.exists() || parent.mkdirs();
            if (canSave) {
                try {
                    canSave = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    canSave = false;
                }
            }
        }
        if (canSave) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                source.compress(compressFormat, quality, fos);
                fos.flush();
                fos.close();
                canSave = true;
            } catch (IOException e) {
                e.printStackTrace();
                canSave = false;
            }
        }
        if (!canSave) {
            try {
                if (file.exists())
                    file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return canSave;
    }
}
