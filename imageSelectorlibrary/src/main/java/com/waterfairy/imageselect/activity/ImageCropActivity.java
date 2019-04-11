package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.options.CompressOptions;
import com.waterfairy.imageselect.options.CropImgOptions;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.utils.FileUtils;
import com.waterfairy.imageselect.utils.ProviderUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 系统 / 第三方 处理
 */
public class ImageCropActivity extends BaseActivity {
    private CropImgOptions cropImgOptions;
    private File saveFile;
    private ImageView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_crop);
        mImg = findViewById(R.id.img);

        cropImgOptions = (CropImgOptions) getIntent().getSerializableExtra(ConstantUtils.OPTIONS_BEAN);
        compressOptions = (CompressOptions) getIntent().getSerializableExtra(ConstantUtils.OPTIONS_COMPRESS_BEAN);
        if (cropImgOptions == null) cropImgOptions = new CropImgOptions();

        File file = new File(cropImgOptions.getImgPath());
        Glide.with(this).load(file).into(mImg);

        Intent intent = new Intent("com.android.camera.action.CROP");
        ProviderUtils.setAuthority(cropImgOptions.getPathAuthority());
        Uri providerUri = ProviderUtils.getProviderUri(this, intent, file);
        intent.setDataAndType(providerUri, "image/*");
        intent.putExtra("crop", "true");
        if (cropImgOptions.getAspectX() > 0 && cropImgOptions.getAspectY() > 0) {
            //设置比例
            intent.putExtra("aspectX", cropImgOptions.getAspectX());
            intent.putExtra("aspectY", cropImgOptions.getAspectY());
        }
        if (cropImgOptions.getWidth() > 0 && cropImgOptions.getHeight() > 0) {
            //根据宽高 设置比例
            intent.putExtra("aspectX", cropImgOptions.getWidth());
            intent.putExtra("aspectY", cropImgOptions.getHeight());
            //设置宽高
            intent.putExtra("outputX", cropImgOptions.getWidth());
            intent.putExtra("outputY", cropImgOptions.getHeight());
        }

        //设置输出格式
        String extension = "jpg";
        String imgPath = cropImgOptions.getImgPath();
        if (imgPath.endsWith(".png") || imgPath.endsWith(".PNG")) {
            extension = "png";
        }
        //设置输出路径
        saveFile = FileUtils.getCropSavePath(cropImgOptions.getCropPath(), extension);
        // 把文件地址转换成Uri格式
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveFile));
        // 设置为true直接返回bitmap
        intent.putExtra("outputFormat", TextUtils.equals(extension, "png") ?
                Bitmap.CompressFormat.PNG.toString() : Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, cropImgOptions.getRequestCode());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ArrayList<String> dataList = new ArrayList<String>();
            dataList.add(saveFile.getAbsolutePath());
            Intent intent = new Intent();
            intent.putExtra(ConstantUtils.RESULT_STRING, dataList);
            setResult(RESULT_OK, intent);
            finish();
//            compress(dataList, cropImgOptions.getCropPath());
//            Glide.with(this).load(saveFile).into(mImg);
        } else {
            finish();
        }
    }

    @Override
    protected String getTag() {
        return cropImgOptions.getTag();
    }
}
