package com.waterfairy.imageselect.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

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

public class TestActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private GridView gridView;
    private View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select_test);
        findViewById(R.id.select_img).setOnClickListener(this);
        findViewById(R.id.take_photo).setOnClickListener(this);
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

    public void selectImg(View view) {
        ArrayList<String> ignore = new ArrayList<>();
        ignore.add(ConstantUtils.PATH_WX);
        ImageSelector.with(this).options(new SelectImgOptions().setGridNum(3).setMaxNum(12).setSearchDeep(3).setLoadCache(false)
                .setSearchPaths(ignore)).compress(new CompressOptions().setMaxHeight(1080).setMaxWidth(1080).setMaxSize(500)).execute();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        int currentPos = data.getIntExtra(ConstantUtils.CURRENT_POS, 0);
        currentView = gridView.getChildAt(currentPos);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ConstantUtils.REQUEST_SELECT || requestCode == ConstantUtils.REQUEST_TAKE_PHOTO) && resultCode == RESULT_OK) {
            ArrayList<String> stringArrayListExtra = data.getStringArrayListExtra(ConstantUtils.RESULT_STRING);
            gridView.setAdapter(new MyAdapter(data.getStringArrayListExtra(ConstantUtils.RESULT_STRING), this));
            Glide.with(this).load(stringArrayListExtra.get(0)).into((ImageView) findViewById(R.id.zoom_img));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<String> dataList = ((MyAdapter) gridView.getAdapter()).getDataList();
//        ImageSelector.with(this).options(new ShowImgOptions().setClickToDismiss(true).setCurrentPos(position).setImgList(dataList)).showImg(view, dataList.get(position));
        ImageSelector.with(this).options(new CropImgOptions().setImgPath(dataList.get(0))).execute();


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.select_img) {
            selectImg(v);
        } else if (v.getId() == R.id.take_photo) {
            ImageSelector.with(this).options(new TakePhotoOptions()).execute();
        }
    }
}
