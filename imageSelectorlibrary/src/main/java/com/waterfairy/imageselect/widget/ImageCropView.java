package com.waterfairy.imageselect.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.waterfairy.imageselect.async.CropBitmapAsync;
import com.waterfairy.imageselect.listener.OnCropBitmapListener;
import com.waterfairy.imageselect.options.CropImgOptions;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019/1/20 16:00
 * @info:
 */
public class ImageCropView extends AppCompatImageView implements LineDrawer.OnLineChangeListener, BitmapDrawer.OnDrawerChangeListener {
    private static final String TAG = "imageCropView";
    private LineDrawer mLineDrawer;//框
    private BitmapDrawer mBitmapDrawer;//图片
    //    private Paint mPaint;//画笔
    private boolean isImgMove;//图片是否移动中
    private CropImgOptions cropImgOptions;
    private OnLongClickListener onLongClickListener;
    private OnClickListener onClickListener;
    private OnDoubleClickListener onDoubleClickListener;


    public ImageCropView(Context context) {
        super(context, null);
    }

    public ImageCropView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        test();
        mLineDrawer = new LineDrawer(context.getResources().getDisplayMetrics().density);
        mLineDrawer.setOnLineChangeListener(this);
        mBitmapDrawer = new BitmapDrawer(this);
        mBitmapDrawer.setOnDrawerChangeListener(this);
    }

//    private void test() {
//        mPaint = new Paint();
//        mPaint.setTextSize(30);
//        mPaint.setColor(Color.RED);
//    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            //整个布局的1/4     边界是布局的短边的1/2  并居中
            float minRadius = Math.min(right - left, bottom - top) / 4;
            float centerX = (right - left) / 2;
            float centerY = (bottom - top) / 2;

            float radio = 1;
            float xRadius = minRadius;
            float yRadius = minRadius;
            if (cropImgOptions != null) {
                radio = cropImgOptions.getRadio();
                if (radio > 1) {
                    //宽大
                    yRadius = xRadius / radio;
                } else {
                    xRadius = yRadius * radio;
                }
            }

            mLineDrawer.setBounds(centerX - xRadius, centerY - yRadius, centerX + xRadius, centerY + yRadius);
            mLineDrawer.setMaxRect(right - left, bottom - top);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mLineDrawer.draw(canvas);
//        canvas.drawText(mLineDrawer.toString(), 10, 25, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //线是否在移动
        boolean isLineMove = false;
        //移动线框 注意 : 不可让边框大于图片的边距
        if (!isImgMove) {
            //刷新图片边距
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mLineDrawer.freshBitmapRect(mBitmapDrawer.getBitmapRect());
            }
            isLineMove = mLineDrawer.isCanMove(event);
        }
        //移动图片
        if (!isLineMove) {
            //刷新剪切框边界
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mBitmapDrawer.freshLineRect(mLineDrawer.getLineRect());
            }
            isImgMove = mBitmapDrawer.isCanMove(event);
        }
        return isLineMove || isImgMove;
    }

    public void startCrop(String cropPath, String absolutePath, OnCropBitmapListener onCropBitmapListener) {
        new CropBitmapAsync()
                .initData(cropPath, absolutePath, Bitmap.CompressFormat.JPEG, getDrawable(), mLineDrawer.getLineRect(), mBitmapDrawer.getBitmapRect(), onCropBitmapListener)
                .start();
    }

    /**
     * 线改变
     *
     * @param lineDrawer
     */
    @Override
    public void onLineChange(LineDrawer lineDrawer) {
        invalidate();
    }

    @Override
    public void onBitmapChange(BitmapDrawer bitmapDrawer) {
        invalidate();
    }


    public void setCompressBean(CropImgOptions cropImgOptions) {
        this.cropImgOptions = cropImgOptions;
        if (this.cropImgOptions != null)
            mLineDrawer.setRatio(cropImgOptions.getRadio());
    }


    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.onClickListener = l;
    }

    @Override
    public void onClickListener() {
        if (onClickListener != null) onClickListener.onClick(this);
    }

    @Override
    public void onDoubleClickListener() {
        if (onDoubleClickListener != null) onDoubleClickListener.onDoubleClick(this);
    }

    public interface OnDoubleClickListener {
        void onDoubleClick(View view);
    }

    public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
    }

    @Override
    public boolean onLongClickListener() {
        if (onLongClickListener != null) onLongClickListener.onLongClick(this);
        return false;
    }
}
