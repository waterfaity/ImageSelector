package com.waterfairy.imageselect.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/3 18:43
 * @info:
 */
public class FileUtils {


    public static File getCropSavePath(String cropPath, String extension) {
        File file = null;
        if (TextUtils.isEmpty(cropPath)) {
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CropPictures");
        } else {
            file = new File(cropPath);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String format = simpleDateFormat.format(new Date());
        return new File(file, "IMG_CROP_" + format + "." + extension);

    }
}


