package com.waterfairy.imageselect.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/10/26
 * @Description:
 */

public class ProviderUtils {
    public static String authority="com.waterfairy.imageselect.fileProvider";


    public static Uri getProviderUri(Context context, File file) {
        return getProviderUri(context, null, file);
    }

    public static Uri getProviderUri(Context context, Intent intent, File file) {
        Uri contentUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (intent != null) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            contentUri = FileProvider.getUriForFile(context, authority, file);
        } else {
            contentUri = Uri.fromFile(file);
        }
        return contentUri;
    }

    public static Intent setUriPermission(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        return intent;
    }
}
