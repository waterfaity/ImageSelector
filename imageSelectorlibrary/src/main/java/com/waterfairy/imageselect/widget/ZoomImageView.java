package com.waterfairy.imageselect.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * @author water_fairy
 * @email 995637517@qq.com
 * @date 2019/4/3 17:23
 * @info:
 */
public class ZoomImageView extends AppCompatImageView implements BitmapDrawer.OnDrawerChangeListener {
    private static final String TAG = "Zoom2ImageView";
    private BitmapDrawer bitmapDrawer;
    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;
    private OnDoubleClickListener onDoubleClickListener;
    private boolean canZoom;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bitmapDrawer = new BitmapDrawer(this);
        bitmapDrawer.setCanTouchArriveEdge(false);
        bitmapDrawer.setOnDrawerChangeListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canZoom) {
            boolean canMove = bitmapDrawer.isCanMove(event);
            getParent().requestDisallowInterceptTouchEvent(canMove);
            if (!canMove) {
                return super.onTouchEvent(event);
            } else {
                return true;
            }
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public void onBitmapChange() {
        invalidate();
    }

    @Override
    public void setOnLongClickListener(@Nullable OnLongClickListener onLongClickListener) {
        if (canZoom) {
            this.onLongClickListener = onLongClickListener;
        } else {
            super.setOnLongClickListener(onLongClickListener);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (canZoom) {
            this.onClickListener = l;
        } else {
            super.setOnClickListener(l);
        }
    }

    @Override
    public void onClick() {
        if (onClickListener != null) onClickListener.onClick(this);
    }

    @Override
    public void onDoubleClick() {
        if (onDoubleClickListener != null) onDoubleClickListener.onDoubleClick(this);
    }

    @Override
    public void onLongClick() {
        if (onLongClickListener != null) onLongClickListener.onLongClick(this);
    }

    public void setOnDoubleClick(OnDoubleClickListener onDoubleClickListener) {
        this.onDoubleClickListener = onDoubleClickListener;
    }

    public void setCanZoom(boolean canZoom) {
        this.canZoom = canZoom;
        if (bitmapDrawer != null) bitmapDrawer.setEnable(canZoom);
    }

    public boolean getCanZoom() {
        return canZoom;
    }

    public interface OnDoubleClickListener {
        void onDoubleClick(View view);
    }
}
