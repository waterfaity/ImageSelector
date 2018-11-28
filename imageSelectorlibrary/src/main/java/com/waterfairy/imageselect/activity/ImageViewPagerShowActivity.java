package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.utils.PathUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageViewPagerShowActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, PhotoViewAttacher.OnPhotoTapListener {
    private ArrayList<String> dataList;
    private ArrayList<String> tempDataList;
    private List<PhotoView> photoViews;
    private ViewPager mVPShowImg;
    private LinearLayout mLLSelect;
    private TextView mTVTitle;
    private CheckBox mCBSelect;
    private int currentPos;
    private Button mBTEnsure;
    private int maxNum;
    private boolean isVisibility = true;
    private RelativeLayout mRLTop, mRLBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_selector_activity_image_view_pager_show);
        getExtra();
        findView();
        initView();
        initData();
    }

    private void initData() {
        tempDataList = new ArrayList<>();
        tempDataList.addAll(dataList);
        setViewPager();
    }

    private void setViewPager() {

        mVPShowImg.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return photoViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView photoView = photoViews.get(position);
                container.addView(photoView);
                photoView.setOnPhotoTapListener(ImageViewPagerShowActivity.this);
                return photoView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(photoViews.get(position));
            }
        });
        if (dataList != null && dataList.size() > 0) {
            mTVTitle.setText(new File(dataList.get(0)).getName());
        }
    }

    private void initView() {
        mLLSelect.setOnClickListener(this);
        mVPShowImg.setOffscreenPageLimit(3);
        mBTEnsure.setOnClickListener(this);
        photoViews = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            PhotoView photoView = new PhotoView(this);
            Glide.with(this).load(dataList.get(i)).into(photoView);
            photoViews.add(photoView);
        }
        mVPShowImg.addOnPageChangeListener(this);
        mBTEnsure.setText("完成(" + dataList.size() + "/" + maxNum + ")");

    }

    private void findView() {
        mVPShowImg = findViewById(R.id.view_pager);
        mLLSelect = findViewById(R.id.select_button_lin);
        mCBSelect = findViewById(R.id.check_box);
        mBTEnsure = findViewById(R.id.ensure_button);
        mRLTop = findViewById(R.id.rel_top);
        mRLBottom = findViewById(R.id.rel_bottom);
        mTVTitle = findViewById(R.id.title);
    }

    @Override
    public void onClick(View v) {
        if (!canClick) return;

        if (v.getId() == R.id.select_button_lin) {
            String path = dataList.get(currentPos);
            if (mCBSelect.isChecked()) {
                tempDataList.remove(path);
                mCBSelect.setChecked(false);
            } else {
                tempDataList.add(path);
                mCBSelect.setChecked(true);
            }

            if (tempDataList.size() == 0) {
                mBTEnsure.setText("完成");
                setEnsureCanClick(false);
            } else {
                mBTEnsure.setText("完成(" + tempDataList.size() + "/" + maxNum + ")");
                setEnsureCanClick(true);
            }
        } else if (v.getId() == R.id.ensure_button) {
            setResult(true);
        }
    }

    public void setEnsureCanClick(boolean canClick) {
        if (canClick) {
            mBTEnsure.setBackgroundResource(R.drawable.image_selector_style_ensure_button);
            mBTEnsure.setTextColor(getResources().getColor(R.color.imageSelectorColorWhite));
            mBTEnsure.setClickable(true);
        } else {
            mBTEnsure.setBackgroundResource(R.drawable.image_selector_style_ensure_button2);
            mBTEnsure.setTextColor(getResources().getColor(R.color.imageSelectorColorEnsureShadow));
            mBTEnsure.setClickable(false);
        }
    }

    private void getExtra() {
        Intent intent = getIntent();
        dataList = intent.getStringArrayListExtra("dataList");
        maxNum = intent.getIntExtra(ConstantUtils.MAX_NUM, 1);
        String ori = intent.getStringExtra(ConstantUtils.SCREEN_DIRECTION);
        setRequestedOrientation(TextUtils.isDigitsOnly(ori) ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : (
                TextUtils.equals(ori, ConstantUtils.SCREEN_PORT) ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE));

    }

    public void setResult(boolean complete) {

        Intent intent = new Intent();
        intent.putStringArrayListExtra("dataList", tempDataList);
        if (complete) {
            intent.putExtra("complete", true);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    public void back(View view) {
        if (!canClick) return;
        setResult(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPos = position;
        String path = dataList.get(position);
        mTVTitle.setText(new File(path).getName());
        if (tempDataList.contains(path)) {
            mCBSelect.setChecked(true);
        } else {
            mCBSelect.setChecked(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onPhotoTap(View view, float x, float y) {
        if (isVisibility) {
            mRLTop.startAnimation(getInAnim(true, false));
            mRLBottom.startAnimation(getInAnim(false, false));
        } else {
            mRLTop.startAnimation(getInAnim(true, true));
            mRLBottom.startAnimation(getInAnim(false, true));
        }
        isVisibility = !isVisibility;
        canClick = isVisibility;
    }


    private TranslateAnimation getInAnim(boolean up, boolean in) {
        float fromValue = -1;
        float toValue = 0;

        if (up) {
            if (!in) {
                fromValue = 0;
                toValue = -1;
            }
        } else {
            if (in) {
                fromValue = 1;
                toValue = 0;
            } else {
                fromValue = 0;
                toValue = 1;
            }

        }
        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, 0,
                Animation.RELATIVE_TO_SELF, fromValue,
                Animation.RELATIVE_TO_SELF, toValue);
        translateAnimation.setDuration(300);
        if (!in) {

            translateAnimation.setFillAfter(true);
        }
        return translateAnimation;
    }

    private boolean canClick = true;

}
