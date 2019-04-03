package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.listener.OnCropBitmapListener;
import com.waterfairy.imageselect.options.CompressOptions;
import com.waterfairy.imageselect.options.CropImgOptions;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.widget.ImageCropView;

import java.io.File;
import java.sql.RowId;
import java.util.ArrayList;

/**
 * 自处理
 */
public class ImageCropSelfActivity extends BaseActivity implements View.OnClickListener {
    private CropImgOptions cropImgOptions;
    private ImageCropView imageCropView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_crop);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.ensure_button).setOnClickListener(this);
        imageCropView = findViewById(R.id.img);

        cropImgOptions = (CropImgOptions) getIntent().getSerializableExtra(ConstantUtils.OPTIONS_BEAN);
        compressOptions = (CompressOptions) getIntent().getSerializableExtra(ConstantUtils.OPTIONS_COMPRESS_BEAN);
        imageCropView.setCompressBean(cropImgOptions);
        File file = new File(cropImgOptions.getImgPath());
        Glide.with(this).load(file).into(imageCropView);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            finish();
        } else if (v.getId() == R.id.ensure_button) {
            cropImg();
        }
    }

    private void cropImg() {
        imageCropView.startCrop(cropImgOptions.getCropPath(), new File(cropImgOptions.getImgPath()).getAbsolutePath(), new OnCropBitmapListener() {
            @Override
            public void onCropSuccess(String bitmapPath) {
                dismissDialog();
                ArrayList<String> dataList = new ArrayList<>();
                dataList.add(bitmapPath);
                compress(dataList);
            }

            @Override
            public void onCropError(String errMsg) {
                showErrorDialog("裁剪", "图片裁剪失败");
            }

            @Override
            public void onCropStart() {
                showDialog("裁剪", "图片裁剪中...");
            }
        });
    }
}
