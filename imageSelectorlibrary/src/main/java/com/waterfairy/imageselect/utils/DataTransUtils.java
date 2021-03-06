package com.waterfairy.imageselect.utils;

import android.text.TextUtils;

import com.waterfairy.imageselect.options.CompressOptions;
import com.waterfairy.imageselect.tool.ImageSelectorShareTool;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019/3/28 11:17
 * @info:
 */
public class DataTransUtils {

    public static String generateFileCompressPath(CompressOptions compressOptions, String sourcePath, String md5FileName) {
        //格式 后缀
        String endFormat = "";
        if (compressOptions.hasExtension()) {
            if (!compressOptions.isFormatToJpg() && (sourcePath.endsWith(".png") || sourcePath.endsWith(".PNG"))) {
                endFormat = ".png";
            } else {
                endFormat = ".jpg";
            }
        }
        return md5FileName + endFormat;
    }

    public static boolean isCompressFileExist(String compressPath) {
        String srcPath = ImageSelectorShareTool.getInstance().getSrcPath(compressPath);
        return TextUtils.isEmpty(srcPath);
    }

    /**
     * 如果是压缩的文件 则返回原图,如果是原图 还是原图
     *
     * @return
     */
    public static String getTransPath(String inputPath) {
        String srcPath = ImageSelectorShareTool.getInstance().getSrcPath(inputPath);
        if (TextUtils.isEmpty(srcPath)) return inputPath;
        else return srcPath;
    }

    /**
     * 查看保存的压缩路径
     *
     * @param compressTag
     * @return
     */
    public static String getCompressPath(String compressTag) {
        String srcPath = ImageSelectorShareTool.getInstance().getSrcPath(compressTag);
        if (TextUtils.isEmpty(srcPath)) return null;
        else return srcPath;
    }
}
