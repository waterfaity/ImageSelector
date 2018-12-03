package com.waterfairy.imageselect.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.waterfairy.imageselect.ImageSelector;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.options.SelectImgOptions;
import com.waterfairy.imageselect.options.ShowImgOptions;
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
        ArrayList<String> ignore=new ArrayList<>();
        ignore.add(ConstantUtils.PATH_WX);
        ImageSelector.with(this).options(new SelectImgOptions().setGridNum(3).setMaxNum(12).setSearchDeep(1).setLoadCache(false)
        .setSearchPaths(ignore)).selectImg();
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
        if (requestCode == ConstantUtils.REQUEST_SELECT && resultCode == RESULT_OK) {
            gridView.setAdapter(new MyAdapter(data.getStringArrayListExtra(ConstantUtils.RESULT_STRING), this));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<String> dataList = ((MyAdapter) gridView.getAdapter()).getDataList();
        ImageSelector.with(this).options(new ShowImgOptions().setClickToDismiss(true).setCurrentPos(position).setImgList(dataList)).showImg(view, dataList.get(position));
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.select_img){
            selectImg(v);
        }
    }
}
