package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.options.CompressOptions;
import com.waterfairy.imageselect.options.TakePhotoOptions;
import com.waterfairy.imageselect.utils.ConstantUtils;
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
public class TakePhotoActivity extends BaseActivity {
    private ImageView mImg;
    private File file;
    private TakePhotoOptions options;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_selector_activity_image_select_take_photo);
        getExtra();
        findView();
        initData();
    }

    private void getExtra() {
        options = (TakePhotoOptions) getIntent().getSerializableExtra(ConstantUtils.OPTIONS_BEAN);
        compressOptions = (CompressOptions) getIntent().getSerializableExtra(ConstantUtils.OPTIONS_COMPRESS_BEAN);
        if (options == null) options = new TakePhotoOptions();
    }

    private void findView() {
        mImg = findViewById(R.id.img);
    }

    private void initData() {

        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        // 根据文件地址创建文件
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String format = simpleDateFormat.format(new Date());
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + "/Camera", "IMG_" + format + ".jpg");
        //创建父类文件夹
        createParentFile();
        // 把文件地址转换成Uri格式
        ProviderUtils.setAuthority(options.getPathAuthority());
        Uri uri = ProviderUtils.getProviderUri(this, intent, file);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, options.getRequestCode());
    }

    private void createParentFile() {
        if (file != null) {
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                boolean mkdirs = parentFile.mkdirs();
            }
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String absolutePath = file.getAbsolutePath();
            Glide.with(this).load(file).into(mImg);

            checkOri(absolutePath);
        } else {
            finish();
        }
    }

    private void checkOri(String absolutePath) {
        ArrayList<String> dataList = new ArrayList<String>();
        dataList.add(absolutePath);
        compress(dataList);
    }

    @Override
    protected String getTag() {
        return options.getTag();
    }
}
