package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.listener.GlideRequestForCenterCropListener;
import com.waterfairy.imageselect.listener.GlideRequestListener;
import com.waterfairy.imageselect.utils.ConstantUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

//import com.github.chrisbanes.photoview.OnPhotoTapListener;
//import com.github.chrisbanes.photoview.PhotoView;


public class ImageViewPagerShowActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ArrayList<String> dataList;
    private List<View> linViewList;
    private ViewPager mVPShowImg;
    private TextView mTVTitle;
    private int mCurrentPos;
    private boolean isVisibility = true;
    private RelativeLayout mRLTop, mRLBottom;
    private int mResImgDefault;
    private TextView mTVNum;


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
        setViewPager();
    }

    private void setViewPager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mVPShowImg.setTransitionName(dataList.get(mCurrentPos));
        }

        mVPShowImg.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return linViewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = linViewList.get(position);
                container.addView(view);
                view.setOnClickListener(ImageViewPagerShowActivity.this);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(linViewList.get(position));
            }
        });
        if (dataList != null && dataList.size() > 0) {
            mTVTitle.setText(new File(dataList.get(0)).getName());
        }
        mVPShowImg.setCurrentItem(mCurrentPos);
    }

    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        mVPShowImg.setOffscreenPageLimit(3);
        linViewList = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            View rootView = LayoutInflater.from(this).inflate(R.layout.image_selector_img, null);
            ImageView photoView = rootView.findViewById(R.id.img);
            RequestBuilder<Drawable> load = Glide.with(this).load(dataList.get(i));
            if (mResImgDefault != 0) {
                load.apply(new RequestOptions().placeholder(mResImgDefault).error(mResImgDefault));
            }
            if (mCurrentPos == i) {
                load.listener(new GlideRequestListener(this, findViewById(R.id.root_view), photoView).setOne(true));
            } else {
                load.listener(new GlideRequestForCenterCropListener(photoView));

            }
            load.into(photoView);
            photoView.setOnClickListener(this);
            linViewList.add(rootView);
        }
        mVPShowImg.addOnPageChangeListener(this);
        if (dataList.size() == 0) {
            mRLBottom.setVisibility(View.GONE);
        }
    }

    private void findView() {
        mVPShowImg = findViewById(R.id.view_pager);
        mRLTop = findViewById(R.id.rel_top);
        mRLBottom = findViewById(R.id.rel_bottom);
        mTVNum = findViewById(R.id.tv_num);
        mTVTitle = findViewById(R.id.title);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ensure_button) {
            closeActivity();
        } else {
            if (isVisibility) {
                mRLTop.startAnimation(getInAnim(true, false));
                if (mRLBottom.getVisibility() == View.VISIBLE)
                    mRLBottom.startAnimation(getInAnim(false, false));
            } else {
                mRLTop.startAnimation(getInAnim(true, true));
                if (mRLBottom.getVisibility() == View.VISIBLE)
                    mRLBottom.startAnimation(getInAnim(false, true));
            }
            isVisibility = !isVisibility;
        }
    }


    private void getExtra() {
        Intent intent = getIntent();
        dataList = intent.getStringArrayListExtra(ConstantUtils.DATA_LIST);
        mResImgDefault = intent.getIntExtra(ConstantUtils.DEFAULT_IMG_RES, 0);
        mCurrentPos = intent.getIntExtra(ConstantUtils.CURRENT_POS, 0);
    }

    public void closeActivity() {
        Intent intent = new Intent();
        intent.putExtra(ConstantUtils.CURRENT_POS, mCurrentPos);
        intent.putExtra(ConstantUtils.IMG_PATH, dataList.get(mCurrentPos));
        setResult(RESULT_OK, intent);
        ActivityCompat.finishAfterTransition(this);
    }

    public void back(View view) {
        closeActivity();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            closeActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTVNum.setText(position + 1 + "/" + dataList.size());
        mCurrentPos = position;
        String path = dataList.get(position);
        mTVTitle.setText(new File(path).getName());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

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


}
