package com.waterfairy.imageselect.utils;

import android.text.TextUtils;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/11/16
 * @Description:
 */

public class PathUtils {
    public static String getPathFromUrl(String url) {
        String nameFromUrl = getNameFromUrl(url);
        if (TextUtils.isEmpty(nameFromUrl)) {
            return null;
        } else {
            if (nameFromUrl.contains(".")) {
                String replace = nameFromUrl.replace(".", "-");
                String[] split = replace.split("-");
                StringBuilder temp = new StringBuilder();
                for (int i = 0; i < split.length - 1; i++) {
                    if (i == split.length - 2) {
                        temp.append(split[i]);
                    } else {
                        temp.append(split[i]).append(".");
                    }
                }
                return temp.toString();
            } else {
                return nameFromUrl;
            }
        }
    }

    public static String getNameFromUrl(String url) {
        String[] split = url.split("/");
        if (split.length > 0) {
            return split[split.length - 1];
        }
        return null;
    }
}
