package com.waterfairy.imageselect.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.adapter.ViewPageShowAdapter;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.utils.ImageUtils;
import com.waterfairy.imageselect.utils.MD5Utils;

import java.io.File;
import java.util.ArrayList;

//import com.github.chrisbanes.photoview.OnPhotoTapListener;
//import com.github.chrisbanes.photoview.PhotoView;


public class ImageViewPagerShowActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, ViewPageShowAdapter.OnViewClickListener {
    private ArrayList<String> dataList;
    private ViewPager mVPShowImg;
    private TextView mTVTitle;
    private int mCurrentPos;
    private boolean isVisibility = true;
    private RelativeLayout mRLTop, mRLBottom;
    private int mResImgDefault;//默认图片
    private TextView mTVNum;//数字显示器
    private RelativeLayout mRLSave;
    //保存
    private boolean mCanSaveImg;//保存图片
    private ImageView mSaveImageView;
    private String mSavePath;
    private String mParentPath;


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
        mVPShowImg.setAdapter(new ViewPageShowAdapter(this, dataList)
                .setCurrentPos(mCurrentPos)
                .setReferToView(findViewById(R.id.root_view))
                .setResImgDefault(mResImgDefault)
                .setOnClickListener(this)
        );
        if (dataList != null && dataList.size() > 0) {
            mTVTitle.setText(new File(dataList.get(0)).getName());
        }
        mVPShowImg.setCurrentItem(mCurrentPos);
    }


    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }
        mRLSave.setVisibility(View.GONE);
        mVPShowImg.setOffscreenPageLimit(3);
        mVPShowImg.addOnPageChangeListener(this);
        if (dataList.size() == 0) {
            mRLBottom.setVisibility(View.GONE);
        }
        mTVNum.setText(mCurrentPos + 1 + "/" + dataList.size());
    }

    private void findView() {
        mRLSave = findViewById(R.id.rel_save);
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
        } else if (v.getId() == R.id.rel_save) {
            mRLSave.startAnimation(getInAnim(false, false));
            mRLSave.setClickable(false);
        }
    }


    private void getExtra() {
        Intent intent = getIntent();
        dataList = intent.getStringArrayListExtra(ConstantUtils.DATA_LIST);
        mResImgDefault = intent.getIntExtra(ConstantUtils.DEFAULT_IMG_RES, 0);
        mCurrentPos = intent.getIntExtra(ConstantUtils.CURRENT_POS, 0);
        mCanSaveImg = intent.getBooleanExtra(ConstantUtils.CAN_SAVE_IMG, false);
        mParentPath = intent.getStringExtra(ConstantUtils.SAVE_PARENT_PATH);
        if (TextUtils.isEmpty(mParentPath)) {
            mParentPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        }
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


    /**
     * 点击保存图片
     *
     * @param view
     */
    public void savePicture(View view) {
        mRLSave.startAnimation(getInAnim(false, false));
        mRLSave.setClickable(false);
        saveImg();
    }

    private void saveImg() {
        if (mSaveImageView != null && !TextUtils.isEmpty(mSavePath)) {
            Drawable drawable = mSaveImageView.getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    //保存
                    String absolutePath = new File(mParentPath, new File(mSavePath).getName()).getAbsolutePath();
                    //判断格式
                    if (!TextUtils.isEmpty(absolutePath)) {
                        String format = ".jpg";
                        Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
                        if (absolutePath.length() >= 4) {
                            String substring = absolutePath.substring(absolutePath.length() - 4, absolutePath.length());
                            if (substring.equals(".png") || substring.equals(".PNG")) {
                                format = ".png";
                                compressFormat = Bitmap.CompressFormat.PNG;
                            }
                        }
                        String savePath = new File(mParentPath, MD5Utils.getMD5Code(absolutePath) + format).getAbsolutePath();
                        boolean b = ImageUtils.saveBitmap(savePath, bitmap, compressFormat, 100);
                        Toast.makeText(this, b ? "已保存到" + savePath : "保存失败!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        }
        Toast.makeText(this, "保存失败!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewOnLongClick(ImageView imageView, String imgPath) {
        //提示保存
        mRLSave.startAnimation(getInAnim(false, true));
        mRLSave.setOnClickListener(this);
        mRLSave.setVisibility(View.VISIBLE);
        mSaveImageView = imageView;
        mSavePath = imgPath;
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


    @Override
    public void onViewClick() {
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
