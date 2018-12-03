package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.options.TakePhotoOptions;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.utils.ImageUtils;
import com.waterfairy.imageselect.utils.MD5Utils;
import com.waterfairy.imageselect.utils.ProviderUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2018/12/2
 * @info:
 */
public class TakePhotoActivity extends AppCompatActivity {
    private ImageView mImg;
    private File file;
    private TakePhotoOptions options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select_take_photo);
        mImg = findViewById(R.id.img);
        options = (TakePhotoOptions) getIntent().getSerializableExtra(ConstantUtils.OPTIONS_BEAN);
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        // 根据文件地址创建文件

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String format = simpleDateFormat.format(new Date());
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "IMG_" + format + ".jpg");
        // 把文件地址转换成Uri格式
        Uri providerUri = ProviderUtils.getProviderUri(this, intent, file);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, providerUri);
        startActivityForResult(intent, options.getRequestCode());
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("test", "onActivityResult: ");
        if (resultCode == RESULT_OK) {
            Glide.with(this).load(file).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) resource;
                    String compressPath = options.getCompressPath();
                    if (TextUtils.isEmpty(compressPath)) {
                        compressPath = new File(getExternalCacheDir(), "img").getAbsolutePath();
                    }
                    String absolutePath = new File(compressPath, MD5Utils.getMD5Code(file.getAbsolutePath()) + ".jpg").getAbsolutePath();
                    ImageUtils.saveBitmap(absolutePath, bitmapDrawable.getBitmap(), Bitmap.CompressFormat.JPEG, 100);
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(absolutePath);
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra(ConstantUtils.RESULT_STRING, dataList);
                    setResult(RESULT_OK, intent);
                    finish();
                    return false;
                }
            }).into(mImg);
            saveImg();
        } else {
            finish();
        }
    }

    private void saveImg() {

    }
}
