package com.waterfairy.imageselect.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.waterfairy.imageselect.options.CompressOptions;
import com.waterfairy.imageselect.tool.CompressTool;
import com.waterfairy.imageselect.utils.ConstantUtils;

import java.io.File;
import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {
    protected CompressOptions compressOptions;
    private AlertDialog alertDialog;

    protected void compress(final ArrayList<String> dataList) {
        if (compressOptions != null) {
            if (alertDialog == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("压缩");
                builder.setMessage("图片压缩中...");
                builder.setCancelable(false);
                alertDialog = builder.create();
            }
            alertDialog.show();
            //压缩
            String compressPath = compressOptions.getCompressPath();
            if (TextUtils.isEmpty(compressPath)) {
                compressPath = new File(getExternalCacheDir(), "img").getAbsolutePath();
            }
            CompressTool.newInstance(compressPath, compressOptions, new CompressTool.OnCompressListener() {
                @Override
                public void onCompressSuccess(ArrayList<String> tempDataList) {
                    alertDialog.dismiss();
                    setResult(tempDataList);
                }

                @Override
                public void onCompressing(Integer pos, int totalSize) {
                    alertDialog.setMessage("图片压缩中(" + (pos + 1) + "/" + totalSize + ")...");
                }

                @Override
                public void onCompressError(String msg, ArrayList<String> sourceList) {
                    if (alertDialog != null && alertDialog.isShowing()) {
                        alertDialog.setCancelable(true);
                        alertDialog.dismiss();
                    }
                    setResult(sourceList);
                }
            }).compress(dataList);
        } else {
            setResult(dataList);
        }
    }

    private void setResult(ArrayList<String> dataList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(ConstantUtils.RESULT_STRING, dataList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
