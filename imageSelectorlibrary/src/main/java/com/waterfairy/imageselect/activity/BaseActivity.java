package com.waterfairy.imageselect.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.waterfairy.imageselect.options.CompressOptions;
import com.waterfairy.imageselect.tool.CompressTool;
import com.waterfairy.imageselect.utils.ConstantUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Handler;

public class BaseActivity extends AppCompatActivity {
    protected CompressOptions compressOptions;
    private AlertDialog alertDialog;

    protected void compress(final ArrayList<String> dataList) {
        if (compressOptions != null) {
            showDialog("压缩", "图片压缩中...");
            //压缩
            String compressPath = compressOptions.getCompressPath();
            if (TextUtils.isEmpty(compressPath)) {
                compressPath = new File(getExternalCacheDir(), "img").getAbsolutePath();
            }
            CompressTool.newInstance(compressPath, compressOptions, new CompressTool.OnCompressListener() {
                @Override
                public void onCompressSuccess(ArrayList<String> tempDataList) {
                    dismissDialog();
                    setResult(tempDataList);
                }

                @Override
                public void onCompressing(Integer pos, int totalSize) {
                    showDialog("压缩", "图片压缩中(" + (pos + 1) + "/" + totalSize + ")...");
                }

                @Override
                public void onCompressError(String msg, ArrayList<String> sourceList) {
                    dismissDialog();
                    setResult(sourceList);
                }
            }).compress(dataList);
        } else {
            setResult(dataList);
        }
    }

    public void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.setCancelable(true);
            alertDialog.dismiss();
        }
    }

    public void setResult(ArrayList<String> dataList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ConstantUtils.RESULT_STRING, dataList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private android.os.Handler handler;

    public void showErrorDialog(String title, String message) {
        showDialog(title, message);
        getHandler().removeMessages(0);
        getHandler().sendEmptyMessageDelayed(0, 1000);
    }

    private android.os.Handler getHandler() {
        if (handler == null) {
            handler = new android.os.Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    dismissDialog();
                }
            };
        }
        return handler;
    }

    public void showDialog(String title, String message) {
        if (alertDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            alertDialog = builder.create();
        }
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.show();
    }
}
