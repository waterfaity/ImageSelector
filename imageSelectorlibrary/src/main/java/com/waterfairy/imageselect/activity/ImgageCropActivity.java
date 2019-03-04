package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.options.CompressOptions;
import com.waterfairy.imageselect.options.CropImgOptions;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.utils.ProviderUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ImgageCropActivity extends BaseActivity {
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
        Intent intent = new Intent("com.android.camera.action.CROP");
        ProviderUtils.setAuthority(cropImgOptions.getPathAuthority());
        Uri providerUri = ProviderUtils.getProviderUri(this, intent, file);
        intent.setDataAndType(providerUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 2);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        String extension = "jpg";
        String imgPath = cropImgOptions.getImgPath();
        if (imgPath.endsWith(".png") || imgPath.endsWith(".PNG")) {
            extension = "png";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String format = simpleDateFormat.format(new Date());
        saveFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "cropPictures");
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        saveFile = new File(saveFile, "IMG_" + format + "." + extension);
        // 把文件地址转换成Uri格式
        intent.putExtra("return-data", false);
        Uri uri = ProviderUtils.getProviderUri(this, intent, saveFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
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
            compress(dataList, cropImgOptions.getCompressPath());
            Glide.with(this).load(saveFile).into(mImg);
        } else {
            finish();
        }
    }
}
