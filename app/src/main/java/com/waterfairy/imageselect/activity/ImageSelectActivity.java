package com.waterfairy.imageselect.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waterfairy.imageselect.R;
import com.waterfairy.imageselect.adapter.ShowFolderAdapter;
import com.waterfairy.imageselect.adapter.ShowImgAdapter;
import com.waterfairy.imageselect.bean.SearchImgBean;
import com.waterfairy.imageselect.presenter.SelectPresenter;
import com.waterfairy.imageselect.utils.AnimUtils;
import com.waterfairy.imageselect.utils.PathUtils;
import com.waterfairy.imageselect.utils.PermissionUtils;
import com.waterfairy.imageselect.utils.ShareTool;
import com.waterfairy.imageselect.view.SelectView;

import java.util.ArrayList;
import java.util.List;

public class ImageSelectActivity extends AppCompatActivity implements SelectView, View.OnClickListener, ShowFolderAdapter.OnClickFolderListener, ShowImgAdapter.OnSelectImgListener, ShowImgAdapter.OnImgClickListener {
    //presenter
    private SelectPresenter mPresenter;
    //view
    private ImageView mIVBack;//返回
    private GridView mGVShowImage;//图片展示
    private ListView mLVShowFolder;//文件夹展示
    private LinearLayout mLLFolder;//文件夹展示框  动画移动
    private LinearLayout mLLFolderSelect;//文件夹选择按钮
    private TextView mTVPath;//文件夹选择按钮中的文件夹名展示
    private TextView mTVPriView;//图片预览
    private Button mBTEnsure;
    private View mIMArrow;
    //constantData
    public static final String RESULT_STRING = "resultString";
    public static final String MAX_NUM = "maxNum";
    public static final String SEARCH_DEEP = "deep";
    public static final String SEARCH_PATHS = "searchPaths";
    public static final String IGNORE_PATHS = "ignorePaths";
    public static final String SCREEN_DIRECTION = "screenDirection";
    public static final String GRID_NUM = "gridNum";//横排数量

    public static final String SCREEN_LAND = "land";//水平
    public static final String SCREEN_PORT = "port";//垂直


    //data
    private boolean isFolderListVisibility;
    private String mResultString = "data";
    private int mGridNum = 3;
    private int mMaxNum = 9;
    private String mScreenDir;
    private int imgWidth;
    private boolean isDestroy;//横竖屏旋转时处理
    //adapter
    private ShowImgAdapter imgAdapter;
    private ShowFolderAdapter folderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtra();
        setContentView(R.layout.image_selector_activity_image_select);
        mPresenter = new SelectPresenter(this);
        findView();
        initView();
        if (PermissionUtils.requestPermission(this, PermissionUtils.REQUEST_STORAGE)) {
            initData();
        }
    }

    /**
     * 获取传递数据
     */
    private void getExtra() {
        Intent intent = getIntent();
        //方向
        String dir = intent.getStringExtra(SCREEN_DIRECTION);
        this.mScreenDir = TextUtils.isEmpty(dir) ? SCREEN_PORT : dir;
        setRequestedOrientation(TextUtils.equals(mScreenDir, SCREEN_PORT) ?
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //列数
        mGridNum = intent.getIntExtra(GRID_NUM, TextUtils.equals(mScreenDir, SCREEN_PORT) ? 3 : 6);
        String resultString = intent.getStringExtra(RESULT_STRING);
        this.mResultString = TextUtils.isEmpty(resultString) ? mResultString : resultString;
        mMaxNum = intent.getIntExtra(ImageSelectActivity.MAX_NUM, mMaxNum);
    }

    private void findView() {
        mIVBack = (ImageView) findViewById(R.id.back);//返回
        mGVShowImage = (GridView) findViewById(R.id.grid_view);//图片展示
        mLVShowFolder = (ListView) findViewById(R.id.list_view);//文件夹展示
        mLLFolder = (LinearLayout) findViewById(R.id.folder_list_lin);//文件夹展示框  动画移动
        mLLFolderSelect = (LinearLayout) findViewById(R.id.folder_select_button_lin);//文件夹选择按钮
        mTVPath = (TextView) findViewById(R.id.path);//文件夹选择按钮中的文件夹名展示
        mTVPriView = (TextView) findViewById(R.id.pri_view);//图片预览
        mBTEnsure = (Button) findViewById(R.id.ensure_button);//确认按钮
        mIMArrow = findViewById(R.id.arrow);
    }

    private void initView() {
        int dividerWidth = (int) (getResources().getDisplayMetrics().density * 5);
        mGVShowImage.setNumColumns(mGridNum);
        mGVShowImage.setHorizontalSpacing(dividerWidth);
        mGVShowImage.setVerticalSpacing(dividerWidth);
        imgWidth = (getResources().getDisplayMetrics().widthPixels - (mGridNum - 1) * dividerWidth) / mGridNum;
        mLLFolderSelect.setOnClickListener(this);
        mTVPriView.setOnClickListener(this);
        mIVBack.setOnClickListener(this);
        mBTEnsure.setOnClickListener(this);
    }


    private void initData() {
        ShareTool.getInstance().initShare(this);
        handler.sendEmptyMessageDelayed(0, 300);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mPresenter.getExtra(getIntent());
            showSearchDialog();
            mPresenter.queryFolders();
        }
    };


    @Override
    public void setEnsureCanClick(boolean canClick) {
        int size = imgAdapter.getSelectList().size();
        if (canClick) {
            mBTEnsure.setText("完成(" + size + "/" + mMaxNum + ")");
            mTVPriView.setText("预览" + "(" + size + ")");
            mTVPriView.setTextColor(Color.WHITE);
            mTVPriView.setOnClickListener(this);
            mBTEnsure.setBackgroundResource(R.drawable.image_selector_style_ensure_button);
            mBTEnsure.setTextColor(getResources().getColor(R.color.imageSelectorColorWhite));
            mBTEnsure.setClickable(true);
        } else {
            mBTEnsure.setText("完成");
            mTVPriView.setText("预览");
            mTVPriView.setOnClickListener(null);
            mTVPriView.setTextColor(Color.parseColor("#88FFFFFF"));
            mBTEnsure.setBackgroundResource(R.drawable.image_selector_style_ensure_button2);
            mBTEnsure.setTextColor(getResources().getColor(R.color.imageSelectorColorEnsureShadow));
            mBTEnsure.setClickable(false);
        }
    }

    @Override
    public void showSearching(String path) {
        if (mTVDialog != null)
            mTVDialog.setText(path);
    }

    @Override
    public void showImgS(List<SearchImgBean> searchImgBeans) {
        if (imgAdapter == null) {
            imgAdapter = new ShowImgAdapter(this, searchImgBeans, imgWidth, mMaxNum);
            imgAdapter.setOnSelectImgListener(this);
            imgAdapter.setOnImgClickListener(this);
            mGVShowImage.setAdapter(imgAdapter);
        } else {
            imgAdapter.setData(searchImgBeans);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back) {
            finish();
        } else if (id == R.id.folder_select_button_lin) {
            if (!isFolderListVisibility) {
                showFolder();
            }
            showFolderLin(!isFolderListVisibility);
        } else if (id == R.id.folder_list_lin) {
            showFolderLin(false);
        } else if (id == R.id.pri_view) {
            priView();
        } else if (id == R.id.ensure_button) {
            ensure();
        }
    }

    private void ensure() {
        if (imgAdapter.getSelectList().size() > 0) {
            setResult(imgAdapter.getSelectList());
        }
    }

    private void setResult(ArrayList<String> dataList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(mResultString, dataList);
        setResult(RESULT_OK, intent);
        finish();
    }


    private void priView() {
        ArrayList<String> selectList = imgAdapter.getSelectList();
        if (selectList == null || selectList.size() == 0) {
            return;
        }
        Intent intent = new Intent(this, ImageViewPagerShowActivity.class);
        intent.putStringArrayListExtra("dataList", selectList);
        intent.putExtra(SCREEN_DIRECTION, mScreenDir);
        intent.putExtra(MAX_NUM, mMaxNum);
        startActivityForResult(intent, 1);
    }

    private void showFolder() {
        if (folderAdapter == null) {
            folderAdapter = new ShowFolderAdapter(this, mPresenter.getFolderData());
            folderAdapter.setOnClickFolderListener(this);
            mLVShowFolder.setAdapter(folderAdapter);
        }
    }

    private void showFolderLin(boolean show) {
        if (show) {
            mIMArrow.setScaleY(-1);
            TranslateAnimation inAnim = AnimUtils.getInAnim(false, true);
            inAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mLLFolder.clearAnimation();
                    mLLFolder.setClickable(true);
                    mLLFolder.setOnClickListener(ImageSelectActivity.this);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {


                }
            });
            inAnim.setInterpolator(new DecelerateInterpolator());
            mLLFolder.startAnimation(inAnim);
            mLLFolder.setVisibility(View.VISIBLE);
        } else {

            mIMArrow.setScaleY(1);
            TranslateAnimation inAnim = AnimUtils.getInAnim(false, false);
            inAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mLLFolder.clearAnimation();
                    mLLFolder.setVisibility(View.GONE);
                    mLLFolder.setOnClickListener(null);
                    mLLFolder.setClickable(false);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            inAnim.setInterpolator(new AccelerateInterpolator());
            mLLFolder.startAnimation(inAnim);
        }
        isFolderListVisibility = show;
    }

    @Override
    public void onClickFolder(int position) {
        setFolderName(position);
        showFolderLin(false);
        mPresenter.queryImg(position);
    }

    @Override
    public void setFolderName(int position) {
        mTVPath.setText(PathUtils.getNameFromUrl(mPresenter.getFolderData().get(position).getPath()));
    }

    @Override
    public void onSelect(ArrayList<String> searchImgBeans) {
        int size = searchImgBeans.size();
        if (size != 0) {
            setEnsureCanClick(true);
        } else {
            setEnsureCanClick(false);
        }
    }

    @Override
    public void onClickImg(String imgPath) {
        Intent intent = new Intent(this, ImageShowActivity.class);
        intent.putExtra("path", imgPath);
        intent.putExtra(SCREEN_DIRECTION, mScreenDir);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            ArrayList<String> dataList = data.getStringArrayListExtra("dataList");
            if (data.getBooleanExtra("complete", false)) {
                setResult(dataList);
            } else {
                if (dataList == null || dataList.size() == 0) {
                    imgAdapter.removeAllSelect();
                    setEnsureCanClick(false);
                } else {
                    imgAdapter.setSelectList(dataList);
                    setEnsureCanClick(true);
                }
            }
        }
    }

    private TextView mTVDialog;
    private Dialog mDialog;

    @Override
    public void showSearchDialog() {
        if (!isDestroy) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.stopSearch();
                    dialog.dismiss();
                }
            });
            builder.setTitle("搜索中...");
            builder.setView(mTVDialog = new TextView(this, null));
            mTVDialog.setTextColor(Color.parseColor("#666666"));
            mTVDialog.setGravity(Gravity.CENTER_VERTICAL);
            mTVDialog.setPadding((int) (getResources().getDisplayMetrics().density * 16), 0, 0, 0);
            mTVDialog.setText("/storage/emulated/0/");
            mDialog = builder.create();
            mDialog.show();
        }
    }

    @Override
    public void dismissDialog() {
        if (mDialog != null) mDialog.dismiss();
    }

    @Override
    public boolean isDestroy() {
        return isDestroy;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.onRequestPermissionsResultForSDCard(permissions, grantResults)) {
            initData();
        } else {
            Toast.makeText(this, "没有内存卡读取权限", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        dismissDialog();
    }
}
