package com.waterfairy.imageselect.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waterfairy.imageselect.ImageSelector;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.options.CompressOptions;
import com.waterfairy.imageselect.options.CropImgOptions;
import com.waterfairy.imageselect.options.SelectImgOptions;
import com.waterfairy.imageselect.options.ShowImgOptions;
import com.waterfairy.imageselect.options.TakePhotoOptions;
import com.waterfairy.imageselect.utils.ConstantUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "test";
    private GridView gridView;
    private View currentView;
    String pathName;
    boolean hasTransAnim;
    private ArrayList<String> resultDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select_test);

        findViewById(R.id.zoom_img).setOnClickListener(this);
        findViewById(R.id.zoom_img).setOnLongClickListener(this);
        findViewById(R.id.show_one).setOnClickListener(this);
        findViewById(R.id.select_img).setOnClickListener(this);
        findViewById(R.id.take_photo).setOnClickListener(this);
        findViewById(R.id.crop).setOnClickListener(this);
        findViewById(R.id.show).setOnClickListener(this);
        findViewById(R.id.crop2).setOnClickListener(this);

        pathName = getIntent().getStringExtra("pathName");
        hasTransAnim = getIntent().getBooleanExtra("hasTransAnim",true);
        gridView = findViewById(R.id.grid_view);
        gridView.setNumColumns(3);
        gridView.setOnItemClickListener(this);
        ActivityCompat.setExitSharedElementCallback(this, new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                super.onMapSharedElements(names, sharedElements);
                if (currentView == null) return;
                sharedElements.put(names.get(0), currentView);
                currentView = null;
            }
        });
    }


    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        int currentPos = data.getIntExtra(ConstantUtils.CURRENT_POS, 0);
        currentView = gridView.getChildAt(currentPos);
    }

    String url;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ConstantUtils.REQUEST_SELECT || requestCode == ConstantUtils.REQUEST_TAKE_PHOTO || requestCode == ConstantUtils.REQUEST_CROP) && resultCode == RESULT_OK) {
            resultDatas = data.getStringArrayListExtra(ConstantUtils.RESULT_STRING);
            url = resultDatas.get(0);
            gridView.setAdapter(new MyAdapter(data.getStringArrayListExtra(ConstantUtils.RESULT_STRING), this));
            Glide.with(this).load(resultDatas.get(0)).into((ImageView) findViewById(R.id.zoom_img));
            String text = "";
            for (int i = 0; i < resultDatas.size(); i++) {
                text += resultDatas.get(i) + ";";
                Log.i("test", "onActivityResult: " + resultDatas.get(i));
            }
            ((TextView) findViewById(R.id.text)).setText(text);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<String> dataList = ((MyAdapter) gridView.getAdapter()).getDataList();
        ImageSelector.with(this).options(new ShowImgOptions().setClickToDismiss(true).setHasTranslateAnim(hasTransAnim).setCurrentPos(position).addImgList(dataList)).showImg(view, dataList.get(position));
//        ImageSelector.with(this).options(new CropImgOptions().setImgPath(dataList.get(0))).execute();
    }

    public void selectImg(View view) {
        ArrayList<String> ignore = new ArrayList<>();
        ignore.add(ConstantUtils.PATH_WX);
        ImageSelector.with(this)
                .options(new SelectImgOptions().setGridNum(4).setMaxNum(9).setSearchDeep(3).setLoadCache(false).addSearchPaths(ignore))
//                .compress(getCompressOptions())
                .execute();
    }

    private CompressOptions getCompressOptions() {
        return new CompressOptions()
                .setMaxWidth(1500)
                .setMaxHeight(1500)
                .setMaxSize(500)
                .setCompressPath("/sdcard/test/img");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.select_img) {
            selectImg(v);
        } else if (v.getId() == R.id.take_photo) {
            ImageSelector.with(this)
                    .options(new TakePhotoOptions().setPathAuthority(pathName))
                    .compress(getCompressOptions())
                    .execute();
        } else if (v.getId() == R.id.crop) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            ImageSelector.with(this).options(new CropImgOptions().setCropPath("/sdcard/test/img").setImgPath(url).setPathAuthority(pathName)).execute();
        } else if (v.getId() == R.id.crop2) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            ImageSelector.with(this).options(new CropImgOptions().setAspectX(1).setAspectY(2).setCropPath("/sdcard/test/img").setImgPath(url).setPathAuthority(pathName).setCropType(CropImgOptions.CROP_TYPE_SElf)).compress(getCompressOptions()).execute();
        } else if (v.getId() == R.id.show) {
            if (resultDatas == null) return;
//            ImageSelector.with(this).options(new ShowImgOptions().addImgList(resultDatas)).execute();
            ImageSelector.with(this).options(new ShowImgOptions().setClickToDismiss(true).setCurrentPos(0).addImgList(resultDatas).setHasTranslateAnim(hasTransAnim)).showImg(findViewById(R.id.zoom_img), resultDatas.get(0));

        } else if (v.getId() == R.id.zoom_img) {
            Log.i(TAG, "onClick: zoom_img");
        } else if (v.getId() == R.id.show_one) {
            if (resultDatas == null) return;
            ImageSelector.with(this).options(new ShowImgOptions().addImgList(resultDatas).setHasTranslateAnim(hasTransAnim)).showImg(findViewById(R.id.zoom_img), resultDatas.get(0));
        }

    }

    @Override
    public boolean onLongClick(View v) {
        Log.i(TAG, "onLongClick: zoom_img");
        return false;
    }
}
