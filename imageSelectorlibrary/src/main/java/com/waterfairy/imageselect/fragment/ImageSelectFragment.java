package com.waterfairy.imageselect.fragment;

import android.app.Activity;
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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.waterfairy.imageselect.activity.ImageShowLandActivity;
import com.waterfairy.imageselect.activity.ImageShowPortActivity;
import com.waterfairy.imageselect.activity.ImageViewPagerShowActivity;
import com.waterfairy.imageselect.activity.ImageViewPagerShowLandActivity;
import com.waterfairy.imageselect.adapter.ShowFolderAdapter;
import com.waterfairy.imageselect.adapter.ShowImgAdapter;
import com.waterfairy.imageselect.bean.SearchImgBean;
import com.waterfairy.imageselect.presenter.SelectPresenter;
import com.waterfairy.imageselect.utils.AnimUtils;
import com.waterfairy.imageselect.utils.ConstantUtils;
import com.waterfairy.imageselect.utils.PathUtils;
import com.waterfairy.imageselect.utils.PermissionUtils;
import com.waterfairy.imageselect.utils.ShareTool;
import com.waterfairy.imageselect.view.SelectView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2017/12/16
 * @Description:
 */

public class ImageSelectFragment extends Fragment implements
        SelectView,
        View.OnClickListener,
        ShowImgAdapter.OnSelectImgListener,
        ShowFolderAdapter.OnClickFolderListener,
        ShowImgAdapter.OnImgClickListener {
    //presenter
    private SelectPresenter mPresenter;
    //view
    private View mRootView;
    private ImageView mIVBack;//返回
    private GridView mGVShowImage;//图片展示
    private ListView mLVShowFolder;//文件夹展示
    private LinearLayout mLLFolder;//文件夹展示框  动画移动
    private LinearLayout mLLFolderSelect;//文件夹选择按钮
    private TextView mTVPath;//文件夹选择按钮中的文件夹名展示
    private TextView mTVPriView;//图片预览
    private Button mBTEnsure;
    private View mIMArrow;
    //data
    private boolean isFolderListVisibility;
    private String mResultString = "data";
    private int mGridNum = 3;
    private int mMaxNum = 9;
    private String mScreenDir;//屏幕方向
    private int imgWidth;
    private boolean isDestroy;//横竖屏旋转时处理
    //adapter
    private ShowImgAdapter imgAdapter;
    private ShowFolderAdapter folderAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.image_selector_activity_image_select, container, false);
        mPresenter = new SelectPresenter(this);
        getExtra();
        findView();
        initView();
        if (PermissionUtils.requestPermission(getActivity(), PermissionUtils.REQUEST_STORAGE)) {
            initData();
        }
        return mRootView;
    }


    /**
     * 获取传递数据
     */
    private void getExtra() {
        Bundle arguments = getArguments();
        //方向
        String dir = arguments.getString(ConstantUtils.SCREEN_DIRECTION);
        this.mScreenDir = TextUtils.isEmpty(dir) ? ConstantUtils.SCREEN_PORT : dir;
        getActivity().setRequestedOrientation(TextUtils.equals(mScreenDir, ConstantUtils.SCREEN_PORT) ?
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //列数
        mGridNum = arguments.getInt(ConstantUtils.GRID_NUM, TextUtils.equals(mScreenDir, ConstantUtils.SCREEN_PORT) ? 3 : 6);
        String resultString = arguments.getString(ConstantUtils.RESULT_STRING);
        this.mResultString = TextUtils.isEmpty(resultString) ? mResultString : resultString;
        mMaxNum = arguments.getInt(ConstantUtils.MAX_NUM, mMaxNum);
    }

    private void findView() {
        mIVBack = (ImageView) mRootView.findViewById(R.id.back);//返回
        mGVShowImage = (GridView) mRootView.findViewById(R.id.grid_view);//图片展示
        mLVShowFolder = (ListView) mRootView.findViewById(R.id.list_view);//文件夹展示
        mLLFolder = (LinearLayout) mRootView.findViewById(R.id.folder_list_lin);//文件夹展示框  动画移动
        mLLFolderSelect = (LinearLayout) mRootView.findViewById(R.id.folder_select_button_lin);//文件夹选择按钮
        mTVPath = (TextView) mRootView.findViewById(R.id.path);//文件夹选择按钮中的文件夹名展示
        mTVPriView = (TextView) mRootView.findViewById(R.id.pri_view);//图片预览
        mBTEnsure = (Button) mRootView.findViewById(R.id.ensure_button);//确认按钮
        mIMArrow = mRootView.findViewById(R.id.arrow);
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
        ShareTool.getInstance().initShare(getActivity());
        handler.sendEmptyMessageDelayed(0, 300);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mPresenter.getExtraBundle(getArguments());
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
            imgAdapter = new ShowImgAdapter(getActivity(), searchImgBeans, imgWidth, mMaxNum);
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
            getActivity().finish();
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
            setResult(imgAdapter.getSelectList());
        }
    }

    /**
     * 返回图片
     *
     * @param dataList
     */
    private void setResult(ArrayList<String> dataList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(mResultString, dataList);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    /**
     * 预览选择的图片
     */
    private void priView() {
        ArrayList<String> selectList = imgAdapter.getSelectList();
        if (selectList == null || selectList.size() == 0) {
            return;
        }
        Intent intent = null;

        if (TextUtils.equals(mScreenDir, ConstantUtils.SCREEN_LAND)) {
            intent = new Intent(getActivity(), ImageViewPagerShowLandActivity.class);
        } else {
            intent = new Intent(getActivity(), ImageViewPagerShowActivity.class);
        }
        intent.putStringArrayListExtra("dataList", selectList);
        intent.putExtra(ConstantUtils.SCREEN_DIRECTION, mScreenDir);
        intent.putExtra(ConstantUtils.MAX_NUM, mMaxNum);

        startActivityForResult(intent, 1);
    }

    /**
     * 展示文件夹数据
     */
    private void showFolder() {
        if (folderAdapter == null) {
            folderAdapter = new ShowFolderAdapter(getActivity(), mPresenter.getFolderData());
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
                    mLLFolder.setOnClickListener(ImageSelectFragment.this);
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
        Intent intent = null;
        if (TextUtils.equals(mScreenDir, ConstantUtils.SCREEN_LAND))
            intent = new Intent(getActivity(), ImageShowLandActivity.class);
        else intent = new Intent(getActivity(), ImageShowPortActivity.class);
        intent.putExtra(ConstantUtils.STR_PATH, imgPath);
        intent.putExtra(ConstantUtils.SCREEN_DIRECTION, mScreenDir);
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, imgPath).toBundle();
        ActivityCompat.startActivity(getActivity(), intent, bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
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
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.stopSearch();
                    dialog.dismiss();
                }
            });
            builder.setTitle("搜索中...");
            builder.setView(mTVDialog = new TextView(getActivity(), null));
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
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.onRequestPermissionsResultForSDCard(permissions, grantResults)) {
            initData();
        } else {
            show("没有内存卡读取权限");
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroy = true;
        dismissDialog();
    }
}
