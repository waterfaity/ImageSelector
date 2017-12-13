package com.waterfairy.imageselect.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

/**
 * Created by water_fairy on 2016/7/29.
 */
public class PermissionUtils {

    /**
     * 位置权限,定位/蓝牙
     */
    public final static int REQUEST_LOCATION = 1;

    /**
     * 文件读写
     */
    public final static int REQUEST_STORAGE = 2;

    /**
     * 相机
     */
    public final static int REQUEST_CAMERA = 3;

    /**
     * 录音
     */
    public final static int REQUEST_RECORD = 4;

    /**
     * 申请权限
     *
     * @param activity Activity
     * @param request  请求类型
     */
    public static boolean requestPermission(Activity activity, int request) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        String[] permissions = null;
        String permission = null;
        switch (request) {
            case REQUEST_LOCATION:
                permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
                permission = Manifest.permission.ACCESS_COARSE_LOCATION;
                break;
            case REQUEST_CAMERA:
                permissions = new String[]{Manifest.permission.CAMERA};
                permission = Manifest.permission.CAMERA;
                break;
            case REQUEST_STORAGE:
                permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE};
                permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
                break;
            case REQUEST_RECORD:
                permissions = new String[]{Manifest.permission.RECORD_AUDIO};
                permission = Manifest.permission.RECORD_AUDIO;
                break;
        }
        boolean hasPermission = false;
        if (permissions != null) {
            hasPermission = requestPermission(activity, permissions, permission, request);
        }
        return hasPermission;
    }

    /**
     * @param activity    activity
     * @param permissions 权限组
     * @param permission  权限
     * @param request     requestCode  Activity中 会返回权限申请状态(类似startActivityForResult)
     */

    public static boolean requestPermission(Activity activity,
                                            @NonNull String[] permissions,
                                            @NonNull String permission,
                                            int request) {
        int permissionCode = checkPermission(activity, permission);
        boolean hasPermission = false;
        if (!(hasPermission = (permissionCode == PackageManager.PERMISSION_GRANTED))) {
            ActivityCompat.requestPermissions(activity, permissions, request);
        }
        return hasPermission;
    }

    /**
     * 检查权限
     *
     * @param context    activity
     * @param permission 某个权限
     * @return {
     */
    public static int checkPermission(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission);
    }

    public static boolean onRequestPermissionsResultForCamera(String resultPermissions[], int grantResults[]) {
        return onRequestPermissionsResult(Manifest.permission.CAMERA, resultPermissions, grantResults);
    }

    public static boolean onRequestPermissionsResultForLocation(String resultPermissions[], int grantResults[]) {
        return onRequestPermissionsResult(Manifest.permission.ACCESS_COARSE_LOCATION, resultPermissions, grantResults);
    }

    public static boolean onRequestPermissionsResultForRecord(String resultPermissions[], int grantResults[]) {
        return onRequestPermissionsResult(Manifest.permission.RECORD_AUDIO, resultPermissions, grantResults);
    }

    public static boolean onRequestPermissionsResultForSDCard(String resultPermissions[], int grantResults[]) {
        return onRequestPermissionsResult(Manifest.permission.WRITE_EXTERNAL_STORAGE, resultPermissions, grantResults);
    }

    public static boolean onRequestPermissionsResult(String requestPermission, String resultPermissions[], int grantResults[]) {
        if (resultPermissions.length > 0) {
            for (int i = 0; i < resultPermissions.length; i++) {
                if (TextUtils.equals(resultPermissions[i], requestPermission)) {
                    if (grantResults.length > i) {
                        return grantResults[i] == PackageManager.PERMISSION_GRANTED;
                    }
                }
            }
        }
        return false;
    }
}
