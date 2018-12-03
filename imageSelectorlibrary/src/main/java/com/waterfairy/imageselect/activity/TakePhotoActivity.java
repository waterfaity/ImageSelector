package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.waterfairy.imageselect.R;

import java.io.File;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/2
 * @info:
 */
public class TakePhotoActivity extends AppCompatActivity {
    private ImageView mImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select_take_photo);
        mImg = findViewById(R.id.img);
//        Intent intent = new Intent();
//        // 指定开启系统相机的Action
//        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.addCategory(Intent.CATEGORY_DEFAULT);
//        // 根据文件地址创建文件
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "");
//        // 把文件地址转换成Uri格式
//        Uri uri = Uri.fromFile(file);
//        // 设置系统相机拍摄照片完成后图片文件的存放地址
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
