package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.options.CropImgOptions;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.utils.ProviderUtils;

import java.io.File;

public class ImgCropActivity extends AppCompatActivity {
    private CropImgOptions cropImgOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_crop);
        cropImgOptions = (CropImgOptions) getIntent().getSerializableExtra(ConstantUtils.OPTIONS_BEAN);


        File file = new File(cropImgOptions.getImgPath());
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri uri = Uri.fromFile(file);// parse(pathUri);
//        ProviderUtils.getProviderUri(this, intent, file);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 2);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        // 设置为true直接返回bitmap
        intent.putExtra("return-data", true);
        // 上面设为false的时候将MediaStore.EXTRA_OUTPUT关联一个Uri

//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, cropImgOptions.getRequestCode());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == cropImgOptions.getRequestCode() && resultCode == RESULT_OK) {
            Log.i("teat", "onActivityResult: ");
        }
    }
}
