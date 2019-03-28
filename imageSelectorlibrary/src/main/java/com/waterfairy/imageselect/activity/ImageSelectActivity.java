package com.waterfairy.imageselect.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
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
import com.waterfairy.imageselect.options.CompressOptions;
import com.waterfairy.imageselect.options.SelectImgOptions;
import com.waterfairy.imageselect.presenter.SelectPresenter;
import com.waterfairy.imageselect.utils.AnimUtils;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.utils.DataTransUtils;
import com.waterfairy.imageselect.utils.PathUtils;
import com.waterfairy.imageselect.utils.PermissionUtils;
import com.waterfairy.imageselect.tool.ImageSelectorShareTool;
import com.waterfairy.imageselect.view.SelectView;

import java.util.ArrayList;
import java.util.List;

public class ImageSelectActivity extends BaseActivity implements SelectView,
        View.OnClickListener,
        ShowImgAdapter.OnSelectImgListener,
        ShowFolderAdapter.OnClickFolderListener,
        ShowImgAdapter.OnImgClickListener {

    //presenter
    private SelectPresenter mPresenter;
    private ImageView mIVBack;//返回
    private GridView mGVShowImage;//图片展示
    private ListView mLVShowFolder;//文件夹展示
    private LinearLayout mLLFolder;//文件夹展示框  动画移动
    private LinearLayout mLLFolderSelect;//文件夹选择按钮
    private ArrayList<String> mHasSelectPath;
    private TextView mTVPath;//文件夹选择按钮中的文件夹名展示
    private TextView mTVPriView;//图片预览
    private Button mBTEnsure;
    private View mIMArrow;
    //data
    private boolean isFolderListVisibility;
    private int imgWidth;
    private boolean isDestroy;//横竖屏旋转时处理
    private SelectImgOptions options;
    //adapter
    private ShowImgAdapter imgAdapter;
    private ShowFolderAdapter folderAdapter;
    //压缩图片

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);
        mPresenter = new SelectPresenter(this);
        getExtra();
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
        options = (SelectImgOptions) intent.getSerializableExtra(ConstantUtils.OPTIONS_BEAN);
        if (options == null) {
            options = new SelectImgOptions();
            options.setLoadCache(intent.getBooleanExtra(ConstantUtils.LOAD_CACHE, false));
            options.addSearchPaths(intent.getStringArrayListExtra(ConstantUtils.SEARCH_PATHS));
            options.addIgnorePaths(intent.getStringArrayListExtra(ConstantUtils.IGNORE_PATHS));
            options.setMaxNum(intent.getIntExtra(ConstantUtils.MAX_NUM, ConstantUtils.DEFAULT_MAX_NUM));
            options.setSearchDeep(intent.getIntExtra(ConstantUtils.SEARCH_DEEP, ConstantUtils.DEFAULT_DEEP));
            options.setGridNum(intent.getIntExtra(ConstantUtils.GRID_NUM, ConstantUtils.DEFAULT_GRID_NUM_MIN));
            options.addHasSelectFiles(intent.getStringArrayListExtra(ConstantUtils.HAS_SELECT_FILES));
        }
        compressOptions = (CompressOptions) intent.getSerializableExtra(ConstantUtils.OPTIONS_COMPRESS_BEAN);
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
        mGVShowImage.setNumColumns(options.getGridNum(this));
        mGVShowImage.setHorizontalSpacing(dividerWidth);
        mGVShowImage.setVerticalSpacing(dividerWidth);
        imgWidth = (getResources().getDisplayMetrics().widthPixels - (options.getGridNum(this) - 1) * dividerWidth) / options.getGridNum(this);
        mLLFolderSelect.setOnClickListener(this);
        mTVPriView.setOnClickListener(this);
        mIVBack.setOnClickListener(this);
        mBTEnsure.setOnClickListener(this);
    }


    private void initData() {
        ImageSelectorShareTool.getInstance().initShare(this);
        handler.sendEmptyMessageDelayed(0, 300);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mPresenter.initData(options);
            showSearchDialog();
            mPresenter.queryFolders();
        }
    };


    @Override
    public void setEnsureCanClick(boolean canClick) {
        int size = imgAdapter.getSelectList().size();
        if (canClick) {
            mBTEnsure.setText("完成(" + size + "/" + options.getMaxNum() + ")");
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
            imgAdapter = new ShowImgAdapter(this, searchImgBeans, imgWidth, options.getMaxNum());
            setHasSelectFiles();
            imgAdapter.setOnSelectImgListener(this);
            imgAdapter.setOnImgClickListener(this);
            mGVShowImage.setAdapter(imgAdapter);
        } else {
            imgAdapter.setData(searchImgBeans);
        }
    }

    /**
     * 设置已经选择的图片 外部传入
     */
    private void setHasSelectFiles() {
        ArrayList<String> hasSelectFiles = options.getHasSelectFiles();
        if (hasSelectFiles != null) {
            boolean add = false;
            for (int i = 0; i < hasSelectFiles.size(); i++) {
                String selectPath = hasSelectFiles.get(i);
                String transPath = DataTransUtils.getTransPath(selectPath);
                if (!TextUtils.isEmpty(transPath)) {
                    imgAdapter.getSelectList().add(transPath);
                    add = true;
                }
            }
            if (add) setEnsureCanClick(true);
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
        if (imgAdapter != null && imgAdapter.getSelectList().size() > 0) {
            compress(imgAdapter.getSelectList());
        }
    }


    /**
     * 预览选择的图片
     */
    private void priView() {
        ArrayList<String> selectList = imgAdapter.getSelectList();
        if (selectList == null || selectList.size() == 0) {
            return;
        }
        Intent intent = new Intent(this, ImageViewPagerPreviewActivity.class);
        intent.putStringArrayListExtra("dataList", selectList);
        intent.putExtra(ConstantUtils.MAX_NUM, options.getMaxNum());
        startActivityForResult(intent, 1);
    }

    /**
     * 展示文件夹数据
     */
    private void showFolder() {
        if (folderAdapter == null) {
            folderAdapter = new ShowFolderAdapter(this, mPresenter.getFolderData());
            folderAdapter.setOnClickFolderListener(this);
            mLVShowFolder.setAdapter(folderAdapter);
        }
    }

    /**
     * 显示文件夹选项
     *
     * @param show
     */
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

    /**
     * 文件夹点击
     *
     * @param position
     */
    @Override
    public void onClickFolder(int position) {
        setFolderName(position);
        showFolderLin(false);
        mPresenter.queryImg(position);
    }

    /**
     * 显示文件夹名字
     *
     * @param position
     */
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

    /**
     * 点击单张图片
     *
     * @param view
     * @param imgPath
     */
    @Override
    public void onClickImg(View view, String imgPath) {
        Intent intent = new Intent(this, ImageShowActivity.class);
        intent.putExtra(ConstantUtils.STR_PATH, imgPath);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, imgPath).toBundle();
        ActivityCompat.startActivity(this, intent, bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            ArrayList<String> dataList = data.getStringArrayListExtra("dataList");
            if (data.getBooleanExtra("complete", false)) {
                compress(dataList);

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
            if (!isDestroy) {
                mDialog = builder.create();
                mDialog.show();
            }
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
    public void show(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.onRequestPermissionsResultForSDCard(permissions, grantResults)) {
            initData();
        } else {
            show("没有内存卡读取权限");
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        dismissDialog();
        ImageSelectorShareTool.getInstance().onDestroy();
    }
}