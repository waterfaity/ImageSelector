package com.waterfairy.imageselect.widget;

import android.content.Context;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019/4/3 17:23
 * @info:
 */
public class Zoom2ImageView extends AppCompatImageView implements BitmapDrawer.OnDrawerChangeListener {
    private static final String TAG = "Zoom2ImageView";
    private BitmapDrawer bitmapDrawer;
    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;

    public Zoom2ImageView(Context context) {
        this(context, null);
    }

    public Zoom2ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmapDrawer = new BitmapDrawer(this);
        bitmapDrawer.setCanTouchArriveEdge(false);
        bitmapDrawer.setOnDrawerChangeListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean canMove = bitmapDrawer.isCanMove(event);
        Log.i(TAG, "onTouchEvent: " + canMove);
        getParent().requestDisallowInterceptTouchEvent(canMove);
        return canMove;
    }

    @Override
    public void onBitmapChange(BitmapDrawer bitmapDrawer) {
        invalidate();
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
    public boolean onLongClickListener() {
        if (onLongClickListener != null) onLongClickListener.onLongClick(this);
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        bitmapDrawer.freshLineRect(new RectF(left, top, right, bottom));
    }
}
